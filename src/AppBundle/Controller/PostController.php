<?php
namespace AppBundle\Controller;
use AppBundle\Entity\Post;
use AppBundle\Entity\Rate;
use AppBundle\Entity\Tag;
use AppBundle\Form\PostReviewType;
use AppBundle\Form\LocalVideoType;
use AppBundle\Form\VideoType;
use AppBundle\Form\PostType;
use MediaBundle\Entity\Media;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\FormError;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;

class PostController extends Controller {
	public function addAction(Request $request) {
		$post = new Post();
		$post->setType("post");
		$form = $this->createForm(new PostType(), $post);
		$em = $this->getDoctrine()->getManager();
		$categories = $em->getRepository("AppBundle:Category")->findAll();

		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			if ($post->getFile() != null) {
				$media = new Media();
				$media->setFile($post->getFile());
				$media->setEnabled(true);
				$media->upload($this->container->getParameter('files_directory'));

				$post->setMedia($media);

				$post->setUser($this->getUser());
				$post->setReview(false);
				$em->persist($media);
				$em->flush();

				$tags_list = explode(",", $post->getTags());
				foreach ($tags_list as $key => $value) {
					$tag = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
					
					if ($tag == null) {
						$tag = new Tag();
						$tag->setName(mb_strtolower($value, 'UTF-8'));
						$em->persist($tag);
						$em->flush();
						$post->addTagslist($tag);
					} else {
						$post->addTagslist($tag);
					}
				}
				$em->persist($post);
				$em->flush();
				$this->addFlash('success', 'Operation has been done successfully');
				return $this->redirect($this->generateUrl('app_post_index'));
			} else {
				$photo_error = new FormError("Required image file");
				$form->get('file')->addError($photo_error);
			}
		}
		return $this->render("AppBundle:Post:add.html.twig", array(
			"form" => $form->createView(),
			"categories"=>$categories
		));
	}

	public function api_add_rateAction($user, $post, $value, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$a = $em->getRepository('AppBundle:Post')->find($post);
		$u = $em->getRepository("UserBundle:User")->find($user);
		$code = "200";
		$message = "";
		$errors = array();
		if (!$u->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		if ($u != null and $a != null) {

			$rate = $em->getRepository('AppBundle:Rate')->findOneBy(array("user" => $u, "post" => $a));
			if ($rate == null) {
				$rate_obj = new Rate();
				$rate_obj->setValue($value);
				$rate_obj->setPost($a);
				$rate_obj->setUser($u);
				$em->persist($rate_obj);
				$em->flush();
				$message = "Your Ratting has been added";
				if ($u->getId() != $a->getUser()->getId()) {
					$stars = "";
					for ($i = 0; $i < $value; $i++) {
						$stars .= "★";
					}
					$tokens[] = $a->getUser()->getToken();

					$messageNotif = array(
						"type" => "post",
						"id" => $a->getId(),
						"title" => $u->getName() . " Rate " . $a->getTitle(),
						"message" => $value . " " . $stars . " Rating",
					);
					$setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());
					$key = $setting->getFirebasekey();
					$message_status = $this->send_notificationToken($tokens, $messageNotif, $key);
				}
			} else {
				$rate->setValue($value);
				$em->flush();
				$message = "Your Ratting has been edit";
				if ($u->getId() != $a->getUser()->getId()) {
					$stars = "";
					for ($i = 0; $i < $value; $i++) {
						$stars .= "★";
					}
					$tokens[] = $a->getUser()->getToken();

					$messageNotif = array(
						"type" => "post",
						"id" => $a->getId(),
						"title" => $u->getName() . " Edit Rate " . $a->getTitle(),
						"message" => $value . " " . $stars . " Rating",
					);
					$setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());
					$key = $setting->getFirebasekey();
					$message_status = $this->send_notificationToken($tokens, $messageNotif, $key);
				}
			}
		} else {
			$code = "500";
			$message = "Sorry, your rate could not be added at this time";
		}
		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $errors,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}

	public function api_add_shareAction(Request $request, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$id = $request->get("id");
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("id" => $id, "enabled" => true));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$post->setShares($post->getShares() + 1);
		$em->flush();
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($post->getShares(), 'json');
		return new Response($jsonContent);
	}

	public function api_add_viewAction(Request $request, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$id = $request->get("id");
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("id" => $id, "enabled" => true));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$post->setViews($post->getViews() + 1);
		$em->flush();
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($post->getViews(), 'json');
		return new Response($jsonContent);
	}

	public function api_allAction(Request $request, $page, $order, $language, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');

		if ($language == 0) {
			$query = $repository->createQueryBuilder('w')
				->where("w.enabled = true")
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		} else {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.languages', 'l')
				->where('l.id in (' . $language . ')', "w.enabled = true")
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		}
		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_categoryAction(Request $request, $page, $order, $language, $category, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		if ($language == 0) {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.categories', 'c')
				->where('c.id = :category', "w.enabled = true")
				->setParameter('category', $category)
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		} else {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.languages', 'l')
				->leftJoin('w.categories', 'c')
				->where('l.id in (' . $language . ')', "w.enabled = true", 'c.id = :category')

				->setParameter('category', $category)
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		}
		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_followAction(Request $request, $page, $language, $user, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		if ($language == 0) {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.user', 'u')
				->leftJoin('u.followers', 'f')
				->where('f.id = ' . $user, "w.enabled = true")
				->addOrderBy('w.created', 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		} else {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.languages', 'l')
				->leftJoin('w.user', 'u')
				->leftJoin('u.followers', 'f')
				->where('l.id in (' . $language . ')', 'f.id =' . $user, "w.enabled = true")
				->addOrderBy('w.created', 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		}
		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_meAction(Request $request, $page, $user, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		$query = $repository->createQueryBuilder('w')
			->where('w.user = :user')
			->setParameter('user', $user)
			->addOrderBy('w.created', 'DESC')
			->addOrderBy('w.id', 'asc')
			->setFirstResult($nombre * $page)
			->setMaxResults($nombre)
			->getQuery();
		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_queryAction(Request $request, $order, $language, $page, $query, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		if ($language == 0) {
			$query_dql = $repository->createQueryBuilder('w')
				->where("w.enabled = true", "LOWER(w.title) like LOWER('%" . $query . "%') OR LOWER(w.tags) like LOWER('%" . $query . "%') ")
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		} else {
			$language = str_replace("_", ",", $language);
			$query_dql = $repository->createQueryBuilder('w')
				->leftJoin('w.languages', 'l')
				->where("w.enabled = true", 'l.id in (' . $language . ')', "LOWER(w.title) like LOWER('%" . $query . "%') OR LOWER(w.tags) like LOWER('%" . $query . "%') ")
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		}
		$posts = $query_dql->getResult();

		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_randomAction(Request $request, $language, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}

		$nombre = 6;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');

		if ($language == 0) {
			$max = sizeof($repository->createQueryBuilder('g')
					->where("g.enabled = true")
					->getQuery()->getResult());

			$query = $repository->createQueryBuilder('g')
				->where("g.enabled = true")
				->orderBy('g.created', 'DESC')
				->setFirstResult(rand(0, $max - 5))
				->setMaxResults($nombre)
				->orderBy('g.views', 'DESC')
				->getQuery();
		} else {
			$max = sizeof($repository->createQueryBuilder('g')
					->leftJoin('g.languages', 'l')
					->where('l.id in (' . $language . ')', "g.enabled = true")

					->getQuery()->getResult());

			$query = $repository->createQueryBuilder('g')
				->leftJoin('g.languages', 'l')
				->where('l.id in (' . $language . ')', "g.enabled = true")

				->setFirstResult(rand(0, $max - 5))
				->orderBy('g.views', 'DESC')
				->setMaxResults($nombre)
				->getQuery();
		}

		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_by_userAction(Request $request, $page, $order, $language, $user, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		if ($language == 0) {
			$query = $repository->createQueryBuilder('w')
				->where('w.user = :user', "w.enabled = true")
				->setParameter('user', $user)
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		} else {
			$query = $repository->createQueryBuilder('w')
				->leftJoin('w.languages', 'l')
				->where('l.id in (' . $language . ')', "w.enabled = true", 'w.user = :user')

				->setParameter('user', $user)
				->addOrderBy('w.' . $order, 'DESC')
				->addOrderBy('w.id', 'asc')
				->setFirstResult($nombre * $page)
				->setMaxResults($nombre)
				->getQuery();
		}
		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_deleteAction(Request $request, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$id = $request->get("id");
		$userId = $request->get("user");
		$userKey = $request->get("key");
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $userId));
		$post = null;
		if ($user->getTrusted()) {
			$post = $em->getRepository("AppBundle:Post")->findOneBy(array("user" => $user, "id" => $id));
		} else {
			$post = $em->getRepository("AppBundle:Post")->findOneBy(array("user" => $user, "id" => $id, "review" => true));
		}
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $userKey) {
			throw new NotFoundHttpException("Page not found");
		}
		$slides = $em->getRepository("AppBundle:Slide")->findBy(array("post" => $post));
		foreach ($slides as $key => $slide) {
			$media_old_slide = null;
			if ($slide->getMedia() != null) {
				$media_old_slide = $slide->getMedia();
			}
			$em->remove($slide);
			$em->flush();
			if ($media_old_slide != null) {
				$media_old_slide->delete($this->container->getParameter('files_directory'));
				$em->remove($media_old_slide);
				$em->flush();
			}
		}

		$media_old = null;
		if ($post->getMedia() != null) {
			$media_old = $post->getMedia();
		}
		$video_old = null;
		if ($post->getLocalvideo() != null) {
			$video_old = $post->getLocalvideo();
		}
		$em->remove($post);
		$em->flush();
		if ($media_old != null) {
			$media_old->delete($this->container->getParameter('files_directory'));
			$em->remove($media_old);
			$em->flush();
		}
		if ($video_old != null) {
			$video_old->delete($this->container->getParameter('files_directory'));
			$em->remove($video_old);
			$em->flush();
		}
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize(1, 'json');
		return new Response($jsonContent);
	}

	public function api_editVideoAction(Request $request, $token) {

		$post_id = str_replace('"', '', $request->get("post"));
		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$post = $em->getRepository('AppBundle:Post')->findOneBy(array("id" => $post_id));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		$post->setCategories(array());
		$post->setLanguages(array());
		$em->flush();
		$em->flush();

		if ($this->getRequest()->files->has('uploaded_file')) {
			$file = $this->getRequest()->files->get('uploaded_file');
			$media = new Media();
			$media_old = $post->getMedia();
			$media->setFile($file);
			$media->setEnabled(true);
			$media->upload($this->container->getParameter('files_directory'));
			$em->persist($media);
			$em->flush();
			$post->setMedia($media);
			$em->flush();
			$media_old->delete($this->container->getParameter('files_directory'));
			$em->remove($media_old);
			$em->flush();
		}
		if ($this->getRequest()->files->has('uploaded_file_video')) {
			$filevideo = $this->getRequest()->files->get('uploaded_file_video');
			$media_video = new Media();
			$media_video_old = $post->getLocalvideo();
			$media_video->setFile($filevideo);
			$media_video->setEnabled(true);
			$media_video->upload($this->container->getParameter('files_directory'));
			$em->persist($media_video);
			$em->flush();
			$post->setLocalvideo($media_video);
			$em->flush();
			$media_video_old->delete($this->container->getParameter('files_directory'));
			$em->remove($media_video_old);
			$em->flush();
		}

		$post->setTitle($title);
		$post->setContent($description);

		foreach ($language_list as $key => $id_language) {
			$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
			if ($language_obj) {
				$post->addlanguage($language_obj);
			}
		}
		foreach ($categories_list as $key => $id_category) {
			$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
			if ($category_obj) {
				$post->addCategory($category_obj);
			}
		}

		$em->flush();

		$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));

		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}
	public function api_editPostAction(Request $request, $token) {

		$post_id = str_replace('"', '', $request->get("post"));
		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$post = $em->getRepository('AppBundle:Post')->findOneBy(array("id" => $post_id));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		$post->setCategories(array());
		$post->setLanguages(array());
		$em->flush();
		$em->flush();

		if ($this->getRequest()->files->has('uploaded_file')) {
			$file = $this->getRequest()->files->get('uploaded_file');
			$media = new Media();
			$media_old = $post->getMedia();
			$media->setFile($file);
			$media->setEnabled(true);
			$media->upload($this->container->getParameter('files_directory'));
			$em->persist($media);
			$em->flush();
			$post->setMedia($media);
			$em->flush();
			$media_old->delete($this->container->getParameter('files_directory'));
			$em->remove($media_old);
			$em->flush();
		}

		$post->setTitle($title);
		$post->setContent($description);

		foreach ($language_list as $key => $id_language) {
			$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
			if ($language_obj) {
				$post->addlanguage($language_obj);
			}
		}
		foreach ($categories_list as $key => $id_category) {
			$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
			if ($category_obj) {
				$post->addCategory($category_obj);
			}
		}

		$em->flush();

		$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));

		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}
	public function api_editYoutubeAction(Request $request, $token) {

		$post_id = str_replace('"', '', $request->get("post"));
		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));
		$youtube = str_replace('"', '', $request->get("youtube"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$post = $em->getRepository('AppBundle:Post')->findOneBy(array("id" => $post_id));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		$post->setCategories(array());
		$post->setLanguages(array());
		$em->flush();
		$em->flush();

		if ($this->getRequest()->files->has('uploaded_file')) {
			$file = $this->getRequest()->files->get('uploaded_file');
			$media = new Media();
			$media_old = $post->getMedia();
			$media->setFile($file);
			$media->setEnabled(true);
			$media->upload($this->container->getParameter('files_directory'));
			$em->persist($media);
			$em->flush();
			$post->setMedia($media);
			$em->flush();
			$media_old->delete($this->container->getParameter('files_directory'));
			$em->remove($media_old);
			$em->flush();
		}

		$post->setVideo($youtube);
		$post->setTitle($title);
		$post->setContent($description);

		foreach ($language_list as $key => $id_language) {
			$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
			if ($language_obj) {
				$post->addlanguage($language_obj);
			}
		}
		foreach ($categories_list as $key => $id_category) {
			$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
			if ($category_obj) {
				$post->addCategory($category_obj);
			}
		}

		$em->flush();

		$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));

		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}

	public function api_getAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->find($id);
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		return $this->render("AppBundle:Post:api_one.html.php", array("post" => $post));
	}

	public function api_get_rateAction($user = null, $post, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$a = $em->getRepository('AppBundle:Post')->find($post);
		$u = $em->getRepository("UserBundle:User")->find($user);
		$code = "200";
		$message = "";
		$errors = array();

		if ($a != null) {
			$rates_1 = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a, "value" => 1));
			$rates_2 = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a, "value" => 2));
			$rates_3 = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a, "value" => 3));
			$rates_4 = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a, "value" => 4));
			$rates_5 = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a, "value" => 5));
			$rates = $em->getRepository('AppBundle:Rate')->findBy(array("post" => $a));
			$rate = null;
			if ($u != null) {
				$rate = $em->getRepository('AppBundle:Rate')->findOneBy(array("user" => $u, "post" => $a));
			}
			if ($rate == null) {
				$code = "202";
			} else {
				$message = $rate->getValue();
			}

			$errors[] = array("name" => "1", "value" => sizeof($rates_1));
			$errors[] = array("name" => "2", "value" => sizeof($rates_2));
			$errors[] = array("name" => "3", "value" => sizeof($rates_3));
			$errors[] = array("name" => "4", "value" => sizeof($rates_4));
			$errors[] = array("name" => "5", "value" => sizeof($rates_5));
			$total = 0;
			$count = 0;
			foreach ($rates as $key => $r) {
				$total += $r->getValue();
				$count++;
			}
			$v = 0;
			if ($count != 0) {
				$v = $total / $count;
			}
			$v2 = number_format((float) $v, 1, '.', '');
			$errors[] = array("name" => "rate", "value" => $v2);

		} else {
			$code = "500";
			$message = "Sorry, your rate could not be added at this time";
		}
		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $errors,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}

	public function api_myAction(Request $request, $page, $user, $token) {
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$nombre = 30;
		$em = $this->getDoctrine()->getManager();
		$imagineCacheManager = $this->get('liip_imagine.cache.manager');
		$repository = $em->getRepository('AppBundle:Post');
		$query = $repository->createQueryBuilder('w')
			->leftJoin('w.user', 'c')
			->where('c.id = :user')
			->setParameter('user', $user)
			->addOrderBy('w.created', 'DESC')
			->addOrderBy('w.id', 'asc')
			->setFirstResult($nombre * $page)
			->setMaxResults($nombre)
			->getQuery();

		$posts = $query->getResult();
		return $this->render('AppBundle:Post:api_all.html.php', array("posts" => $posts));
	}

	public function api_uploadPostAction(Request $request, $token) {

		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		if ($user) {

			if ($this->getRequest()->files->has('uploaded_file')) {
				$file = $this->getRequest()->files->get('uploaded_file');

				$media = new Media();
				$media->setFile($file);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();

				$post = new Post();
				if (!$user->getTrusted()) {
					$post->setEnabled(false);
					$post->setReview(true);
				} else {
					$post->setEnabled(true);
					$post->setReview(false);
				}
				$post->setType("post");
				$post->setComment(true);
				$post->setTitle($title);
				$post->setContent($description);
				$post->setUser($user);
				$post->setMedia($media);

				foreach ($language_list as $key => $id_language) {
					$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
					if ($language_obj) {
						$post->addlanguage($language_obj);
					}
				}
				foreach ($categories_list as $key => $id_category) {
					$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
					if ($category_obj) {
						$post->addCategory($category_obj);
					}
				}

				$em->persist($post);
				$em->flush();

				if ($user->getTrusted()) {
					$this->sendNotif($em, $post);
				}
				$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));
			}
		}
		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}
	public function api_uploadYoutubeAction(Request $request, $token) {

		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));
		$youtube = str_replace('"', '', $request->get("youtube"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		if ($user) {

			if ($this->getRequest()->files->has('uploaded_file')) {
				$file = $this->getRequest()->files->get('uploaded_file');

				$media = new Media();
				$media->setFile($file);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();

				$post = new Post();
				if (!$user->getTrusted()) {
					$post->setEnabled(false);
					$post->setReview(true);
				} else {
					$post->setEnabled(true);
					$post->setReview(false);
				}
				$post->setType("youtube");
				$post->setVideo($youtube);
				$post->setComment(true);
				$post->setTitle($title);
				$post->setContent($description);
				$post->setUser($user);
				$post->setMedia($media);

				foreach ($language_list as $key => $id_language) {
					$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
					if ($language_obj) {
						$post->addlanguage($language_obj);
					}
				}
				foreach ($categories_list as $key => $id_category) {
					$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
					if ($category_obj) {
						$post->addCategory($category_obj);
					}
				}

				$em->persist($post);
				$em->flush();

				if ($user->getTrusted()) {
					$this->sendNotif($em, $post);
				}
				$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));
			}
		}
		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}
	public function api_uploadVideoAction(Request $request, $token) {

		$id = str_replace('"', '', $request->get("id"));
		$key = str_replace('"', '', $request->get("key"));
		$title = str_replace('"', '', $request->get("title"));
		$description = str_replace('"', '', $request->get("description"));

		$language_ids = str_replace('"', '', $request->get("language"));
		$language_list = explode("_", $language_ids);

		$categories_ids = str_replace('"', '', $request->get("categories"));
		$categories_list = explode("_", $categories_ids);

		$code = "200";
		$message = "Ok";
		$values = array();
		if ($token != $this->container->getParameter('token_app')) {
			throw new NotFoundHttpException("Page not found");
		}
		$em = $this->getDoctrine()->getManager();
		$user = $em->getRepository('UserBundle:User')->findOneBy(array("id" => $id));
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		if (sha1($user->getPassword()) != $key) {
			throw new NotFoundHttpException("Page not found");
		}
		if (!$user->isEnabled()) {
			throw new NotFoundHttpException("Page not found");
		}
		if ($user) {

			if ($this->getRequest()->files->has('uploaded_file') and $this->getRequest()->files->has('uploaded_file_video')) {
				$file = $this->getRequest()->files->get('uploaded_file');

				$media = new Media();
				$media->setFile($file);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();

				$postfile = $this->getRequest()->files->get('uploaded_file_video');
				$postmedia = new Media();
				$postmedia->setFile($postfile);
				$postmedia->upload($this->container->getParameter('files_directory'));
				$em->persist($postmedia);
				$em->flush();

				$post = new Post();
				if (!$user->getTrusted()) {
					$post->setEnabled(false);
					$post->setReview(true);
				} else {
					$post->setEnabled(true);
					$post->setReview(false);
				}
				$post->setType("video");
				$post->setLocalvideo($postmedia);
				$post->setComment(true);
				$post->setTitle($title);
				$post->setContent($description);
				$post->setUser($user);
				$post->setMedia($media);

				foreach ($language_list as $key => $id_language) {
					$language_obj = $em->getRepository('AppBundle:Language')->find($id_language);
					if ($language_obj) {
						$post->addlanguage($language_obj);
					}
				}
				foreach ($categories_list as $key => $id_category) {
					$category_obj = $em->getRepository('AppBundle:Category')->find($id_category);
					if ($category_obj) {
						$post->addCategory($category_obj);
					}
				}

				$em->persist($post);
				$em->flush();

				if ($user->getTrusted()) {
					$this->sendNotif($em, $post);
				}
				$values = array(array("name" => "id", "value" => $post->getId()), array("name" => "title", "value" => $post->getTitle()));
			}
		}
		$error = array(
			"code" => $code,
			"message" => $message,
			"values" => $values,
		);
		$encoders = array(new XmlEncoder(), new JsonEncoder());
		$normalizers = array(new ObjectNormalizer());
		$serializer = new Serializer($normalizers, $encoders);
		$jsonContent = $serializer->serialize($error, 'json');
		return new Response($jsonContent);
	}

	public function deleteAction($id, Request $request) {
		$em = $this->getDoctrine()->getManager();

		$post = $em->getRepository("AppBundle:Post")->find($id);
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}

		$form = $this->createFormBuilder(array('id' => $id))
			->add('id', 'hidden')
			->add('Yes', 'submit')
			->getForm();
		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			$slides = $em->getRepository("AppBundle:Slide")->findBy(array("post" => $post));
			foreach ($slides as $key => $slide) {
				$media_old_slide = null;
				if ($slide->getMedia() != null) {
					$media_old_slide = $slide->getMedia();
				}
				$em->remove($slide);
				$em->flush();
				if ($media_old_slide != null) {
					$media_old_slide->delete($this->container->getParameter('files_directory'));
					$em->remove($media_old_slide);
					$em->flush();
				}
			}

			$media_old = null;
			if ($post->getMedia() != null) {
				$media_old = $post->getMedia();
			}
			$video_old = null;
			if ($post->getLocalvideo() != null) {
				$video_old = $post->getLocalvideo();
			}
			$em->remove($post);
			$em->flush();
			if ($media_old != null) {
				$media_old->delete($this->container->getParameter('files_directory'));
				$em->remove($media_old);
				$em->flush();
			}
			if ($video_old != null) {
				$video_old->delete($this->container->getParameter('files_directory'));
				$em->remove($video_old);
				$em->flush();
			}
			$this->addFlash('success', 'Operation has been done successfully');
			return $this->redirect($this->generateUrl('app_post_index'));
		}
		return $this->render('AppBundle:Post:delete.html.twig', array("form" => $form->createView()));
	}

	public function editAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("id" => $id, "review" => false));
		$categories = $em->getRepository("AppBundle:Category")->findAll();
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$tags = "";
		foreach ($post->getTagslist() as $key => $value) {
			if ($key == sizeof($post->getTagslist()) - 1) {
				$tags .= $value->getName();
			} else {
				$tags .= $value->getName() . ",";
			}
		}
		$post->setTags($tags);

		$form = $this->createForm(new PostType(), $post);
		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			$post->setTagslist(array());
			$em->persist($post);
			$em->flush();

			$tags_list = explode(",", $post->getTags());
			foreach ($tags_list as $k => $v) {
				$tags_list[$k] = mb_strtolower($v, 'UTF-8');
			}
			$tags_list = array_unique($tags_list);

			foreach ($tags_list as $key => $value) {
				$tag = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
				if ($tag == null) {
					$tag = new Tag();
					$tag->setName(mb_strtolower($value, 'UTF-8'));
					$em->persist($tag);
					$em->flush();
					$post->addTagslist($tag);
				} else {
					$post->addTagslist($tag);
				}
			}
			if ($post->getFile() != null) {
				$media = new Media();
				$media_old = $post->getMedia();
				$media->setFile($post->getFile());
				$media->setEnabled(true);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();
				$post->setMedia($media);
				$em->flush();
				$media_old->delete($this->container->getParameter('files_directory'));
				$em->remove($media_old);
				$em->flush();
			}
			$em->persist($post);
			$em->flush();
			$this->addFlash('success', 'Operation has been done successfully');
			return $this->redirect($this->generateUrl('app_post_index'));
		}

		return $this->render("AppBundle:Post:edit.html.twig", array("post" => $post,"categories"=>$categories, "form" => $form->createView()));
	}

	public function indexAction(Request $request) {

		$em = $this->getDoctrine()->getManager();
		$q = "  ";
		if ($request->query->has("query") and $request->query->get("query") != "") {
			$q .= " AND  i.title like '%" . $request->query->get("query") . "%'";
		}

		$dql = "SELECT i FROM AppBundle:Post i  WHERE i.review = false " . $q . " ORDER BY i.created desc ";
		$query = $em->createQuery($dql);
		$paginator = $this->get('knp_paginator');
		$post_list = $paginator->paginate(
			$query,
			$request->query->getInt('page', 1),
			12
		);
		$post_count = $em->getRepository('AppBundle:Post')->countAll();
		return $this->render('AppBundle:Post:index.html.twig', array("post_list" => $post_list, "post_count" => $post_count));
	}

	public function local_addAction(Request $request) {
		$post = new Post();
		$em = $this->getDoctrine()->getManager();
		$categories = $em->getRepository("AppBundle:Category")->findAll();

		$form = $this->createForm(new LocalVideoType(), $post);
		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			if ($post->getFile() != null || $post->getVideofile() != null) {
				// add media
				$file = $post->getFile();
				$media = new Media();
				$media->setFile($file);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();

				$postfile = $post->getVideofile();
				$postmedia = new Media();
				$postmedia->setFile($postfile);
				$postmedia->upload($this->container->getParameter('files_directory'));
				$em->persist($postmedia);
				$em->flush();

				$url = $this->container->getParameter('files_directory') . $media->getUrl();
				$post->setUser($this->getUser());
				$post->setType("video");
				$post->setMedia($media);
				$post->setLocalvideo($postmedia);

				$tags_list = explode(",", $post->getTags());
				foreach ($tags_list as $key => $value) {
					$tag = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
					if ($tag == null) {
						$tag = new Tag();
						$tag->setName(mb_strtolower($value, 'UTF-8'));
						$em->persist($tag);
						$em->flush();
						$post->addTagslist($tag);
					} else {
						$post->addTagslist($tag);
					}
				}



				$em->flush();
				$em->persist($post);
				$em->flush();
				$this->addFlash('success', 'Operation has been done successfully');
				return $this->redirect($this->generateUrl('app_post_index'));
			} else {
				$error = new FormError("Required image file");
				$form->get('file')->addError($error);
				$form->get('localvideo')->addError($error);
			}
		} else {

		}
		return $this->render("AppBundle:Post:local_add.html.twig", array("categories"=>$categories,"form" => $form->createView()));
	}

	public function local_editAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("id" => $id, "review" => false, 'type' => "video"));
		$categories = $em->getRepository("AppBundle:Category")->findAll();

		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$tags = "";
		foreach ($post->getTagslist() as $key => $value) {
			if ($key == sizeof($post->getTagslist()) - 1) {
				$tags .= $value->getName();
			} else {
				$tags .= $value->getName() . ",";
			}
		}
		$post->setTags($tags);



		$form = $this->createForm(new LocalVideoType(), $post);

		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			$post->setTagslist(array());
			$em->persist($post);
			$em->flush();

			$tags_list = explode(",", $post->getTags());
			foreach ($tags_list as $k => $v) {
				$tags_list[$k] =mb_strtolower($v, 'UTF-8');
			}
			$tags_list = array_unique($tags_list);

			foreach ($tags_list as $key => $value) {
				$keyword = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
				if ($keyword == null) {
					$keyword = new Tag();
					$keyword->setName(mb_strtolower($value, 'UTF-8'));
					$em->persist($keyword);
					$em->flush();
					$post->addTagslist($keyword);
				} else {
					$post->addTagslist($keyword);
				}
			}



			if ($post->getFile() != null) {
				$media = new Media();
				$media_old = $post->getMedia();
				$media->setFile($post->getFile());
				$media->setEnabled(true);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();
				$post->setMedia($media);
				$em->flush();
				$media_old->delete($this->container->getParameter('files_directory'));
				$em->remove($media_old);
				$em->flush();

				$url = $this->container->getParameter('files_directory') . $media->getUrl();


			}

			if ($post->getVideofile() != null) {
				$mediavideo = new Media();
				$mediavideo_old = $post->getLocalvideo();
				$mediavideo->setFile($post->getVideofile());
				$mediavideo->setEnabled(true);
				$mediavideo->upload($this->container->getParameter('files_directory'));
				$em->persist($mediavideo);
				$em->flush();
				$post->setLocalvideo($mediavideo);
				$em->flush();
				$mediavideo_old->delete($this->container->getParameter('files_directory'));
				$em->remove($mediavideo_old);
				$em->flush();
			}
			$em->persist($post);
			$em->flush();

			$this->addFlash('success', 'Operation has been done successfully');
			return $this->redirect($this->generateUrl('app_post_index'));
		}
		return $this->render("AppBundle:Post:local_edit.html.twig", array("categories"=>$categories,"form" => $form->createView()));
	}


	public function reviewAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$categories = $em->getRepository("AppBundle:Category")->findAll();
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("id" => $id, "review" => true));
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$tags = "";
		foreach ($post->getTagslist() as $key => $value) {
			if ($key == sizeof($post->getTagslist()) - 1) {
				$tags .= $value->getName();
			} else {
				$tags .= $value->getName() . ",";
			}
		}
		$post->setTags($tags);
		$form = $this->createForm(new PostReviewType(), $post);
		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			$post->setTagslist(array());
			$em->persist($post);
			$em->flush();

			$tags_list = explode(",", $post->getTags());
			foreach ($tags_list as $k => $v) {
				$tags_list[$k] =mb_strtolower($v, 'UTF-8');
			}
			$tags_list = array_unique($tags_list);

			foreach ($tags_list as $key => $value) {
				$keyword = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
				if ($keyword == null) {
					$keyword = new Tag();
					$keyword->setName(mb_strtolower($value, 'UTF-8'));
					$em->persist($keyword);
					$em->flush();
					$post->addTagslist($keyword);
				} else {
					$post->addTagslist($keyword);
				}
			}



			$post->setReview(false);
			$post->setEnabled(true);
			$post->setCreated(new \DateTime());
			$em->persist($post);
			$em->flush();
			$this->addFlash('success', 'Operation has been done successfully');
			return $this->redirect($this->generateUrl('app_home_notif_user_post', array("post_id" => $post->getId())));
		}
		return $this->render("AppBundle:Post:review.html.twig", array("categories"=>$categories,"post" => $post, "form" => $form->createView()));
	}

	public function reviewsAction(Request $request) {

		$em = $this->getDoctrine()->getManager();

		$dql = "SELECT w FROM AppBundle:Post w  WHERE w.review = true ORDER BY w.created desc ";
		$query = $em->createQuery($dql);
		$paginator = $this->get('knp_paginator');
		$posts = $paginator->paginate(
			$query,
			$request->query->getInt('page', 1),
			12
		);
		$post_count = $em->getRepository('AppBundle:Post')->countReview();

		return $this->render('AppBundle:Post:reviews.html.twig', array("posts" => $posts, "post_count" => $post_count));
	}

	public function sendNotif($em, $selected_post) {
		$user = $selected_post->getUser();
		if ($user == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$tokens = array();

		$tokens[] = $user->getToken();
		$message = array(
			"type" => "post",
			"id" => $selected_post->getId(),
			"title" => "👍👍 Post approved ❤️❤️",
			"message" => "😀😀 Congratulation you post has been approved ❤️❤️",
			"image" => "",
			"icon" => "",
		);

		$setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());
		$key = $setting->getFirebasekey();
		$message_image = $this->send_notificationToken($tokens, $message, $key);
	}

	function send_notificationToken($tokens, $message, $key) {
		$url = 'https://fcm.googleapis.com/fcm/send';
		$fields = array(
			'registration_ids' => $tokens,
			'data' => $message,

		);
		$headers = array(
			'Authorization:key = ' . $key,
			'Content-Type: application/json',
		);
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
		$result = curl_exec($ch);
		if ($result === FALSE) {
			die('Curl failed: ' . curl_error($ch));
		}
		curl_close($ch);
		return $result;
	}

	public function shareAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->find($id);
		$setting = $em->getRepository("AppBundle:Settings")->findOneBy(array());
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}

		return $this->render("AppBundle:Post:share.html.twig", array("post" => $post, "setting" => $setting));
	}

	public function viewAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->find($id);
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
	 $rates_1 = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post,"value"=>1));
        $rates_2 = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post,"value"=>2));
        $rates_3 = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post,"value"=>3));
        $rates_4 = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post,"value"=>4));
        $rates_5 = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post,"value"=>5));
        $rates = $em->getRepository('AppBundle:Rate')->findBy(array("post"=>$post));


        $ratings["rate_1"]=sizeof($rates_1);
        $ratings["rate_2"]=sizeof($rates_2);
        $ratings["rate_3"]=sizeof($rates_3);
        $ratings["rate_4"]=sizeof($rates_4);
        $ratings["rate_5"]=sizeof($rates_5);


        $t = sizeof($rates_1) + sizeof($rates_2) +sizeof($rates_3)+ sizeof($rates_4) + sizeof($rates_5);
        if ($t == 0) {
            $t=1;
        }
        $values["rate_1"]=(sizeof($rates_1)*100)/$t;
        $values["rate_2"]=(sizeof($rates_2)*100)/$t;
        $values["rate_3"]=(sizeof($rates_3)*100)/$t;
        $values["rate_4"]=(sizeof($rates_4)*100)/$t;
        $values["rate_5"]=(sizeof($rates_5)*100)/$t;

        $total=0;
        $count=0;
        foreach ($rates as $key => $r) {
           $total+=$r->getValue();
           $count++;
        }
        $v=0;
        if ($count != 0) {
            $v=$total/$count;
        }
        $rating=$v;
		return $this->render("AppBundle:Post:view.html.twig", array("rating"=>$rating,"ratings"=>$ratings,"values"=>$values,"post" => $post));
	}

	public function youtube_addAction(Request $request) {
		$post = new Post();
		$em = $this->getDoctrine()->getManager();
		$categories = $em->getRepository("AppBundle:Category")->findAll();
		$form = $this->createForm(new VideoType(), $post);
		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			if ($post->getFile() != null) {
				// add media
				$file = $post->getFile();
				$media = new Media();
				$media->setFile($file);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();

				$post->setUser($this->getUser());
				$post->setType("youtube");
				$post->setMedia($media);

				$tags_list = explode(",", $post->getTags());
				foreach ($tags_list as $key => $value) {
					$tag = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
					if ($tag == null) {
						$tag = new Tag();
						$tag->setName(mb_strtolower($value, 'UTF-8'));
						$em->persist($tag);
						$em->flush();
						$post->addTagslist($tag);
					} else {
						$post->addTagslist($tag);
					}
				}

				$em->flush();
				$em->persist($post);
				$em->flush();
				$this->addFlash('success', 'Operation has been done successfully');
				return $this->redirect($this->generateUrl('app_post_index'));
			} else {
				$error = new FormError("Required image file");
				$form->get('file')->addError($error);
			}
		}
		return $this->render("AppBundle:Post:youtube_add.html.twig", array("categories"=>$categories,"form" => $form->createView()));
	}

	public function youtube_editAction(Request $request, $id) {
		$em = $this->getDoctrine()->getManager();
		$post = $em->getRepository("AppBundle:Post")->findOneBy(array("type" => "youtube", "id" => $id, "review" => false));
		$categories = $em->getRepository("AppBundle:Category")->findAll();
		
		if ($post == null) {
			throw new NotFoundHttpException("Page not found");
		}
		$tags = "";
		foreach ($post->getTagslist() as $key => $value) {
			if ($key == sizeof($post->getTagslist()) - 1) {
				$tags .= $value->getName();
			} else {
				$tags .= $value->getName() . ",";
			}
		}
		$post->setTags($tags);




		$form = $this->createForm(new VideoType(), $post);

		$form->handleRequest($request);
		if ($form->isSubmitted() && $form->isValid()) {
			$post->setTagslist(array());
			$em->persist($post);
			$em->flush();

			$tags_list = explode(",", $post->getTags());
			foreach ($tags_list as $k => $v) {
				$tags_list[$k] =mb_strtolower($v, 'UTF-8');
			}
			$tags_list = array_unique($tags_list);

			foreach ($tags_list as $key => $value) {
				$keyword = $em->getRepository("AppBundle:Tag")->findOneBy(array("name" => mb_strtolower($value, 'UTF-8')));
				if ($keyword == null) {
					$keyword = new Tag();
					$keyword->setName(mb_strtolower($value, 'UTF-8'));
					$em->persist($keyword);
					$em->flush();
					$post->addTagslist($keyword);
				} else {
					$post->addTagslist($keyword);
				}
			}



			if ($post->getFile() != null) {
				$media = new Media();
				$media_old = $post->getMedia();
				$media->setFile($post->getFile());
				$media->setEnabled(true);
				$media->upload($this->container->getParameter('files_directory'));
				$em->persist($media);
				$em->flush();
				$post->setMedia($media);
				$em->flush();
				$media_old->delete($this->container->getParameter('files_directory'));
				$em->remove($media_old);
				$em->flush();

				$url = $this->container->getParameter('files_directory') . $media->getUrl();


			}

			$em->persist($post);
			$em->flush();

			$this->addFlash('success', 'Operation has been done successfully');
			return $this->redirect($this->generateUrl('app_post_index'));
		}
		return $this->render("AppBundle:Post:youtube_edit.html.twig", array("categories"=>$categories,"form" => $form->createView()));
	}
}
?>