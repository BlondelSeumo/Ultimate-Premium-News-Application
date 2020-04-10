<?php

namespace AppBundle\Controller;
use AppBundle\Entity\Comment;
use AppBundle\Entity\Device;
use AppBundle\Form\SettingsType;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\UrlType;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;


class HomeController extends Controller
{
    function send_notificationToken ($tokens, $message,$key)
    {
        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            'registration_ids'  => $tokens,
            'data'   => $message

            );
        $headers = array(
            'Authorization:key = '.$key,
            'Content-Type: application/json'
            );
       $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
    }
    function send_notification ($tokens, $message,$key)
    {
        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            'to'  => '/topics/NewsAllInOne',
            'data'   => $message
            );
        $headers = array(
            'Authorization:key = '.$key,
            'Content-Type: application/json'
            );
       $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);           
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
    }

   
    public function notifCategoryAction(Request $request)
    {
        memory_get_peak_usage();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');



        $em=$this->getDoctrine()->getManager();
        $categories= $em->getRepository("AppBundle:Category")->findAll();

        $devices= $em->getRepository('AppBundle:Device')->findAll();
        $tokens=array();
        foreach ($devices as $key => $device) {
           $tokens[]=$device->getToken();
        }

        $defaultData = array();
        $form = $this->createFormBuilder($defaultData)
            ->setMethod('GET')
            ->add('title', TextType::class)
            ->add('message', TextareaType::class)
           # ->add('url', UrlType::class)
           # ->add('categories', ChoiceType::class, array('choices' => $categories ))           
            ->add('category', 'entity', array('class' => 'AppBundle:Category'))           
            ->add('icon', UrlType::class,array("label"=>"Large Icon","required"=>false))
            ->add('image', UrlType::class,array("label"=>"Big Picture","required"=>false))
            ->add('send', SubmitType::class,array("label"=>"Send notification"))
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // data is an array with "name", "email", and "message" keys
            $data = $form->getData();

            $category_selected = $em->getRepository("AppBundle:Category")->find($data["category"]);

            $message = array(
                        "type"=>"category",
                        "id"=>$category_selected->getId(),
                        "title_category"=>$category_selected->getTitle(),
                        "language"=>$category_selected->getLanguage()->getId(),
                        "video_category"=>$imagineCacheManager->getBrowserPath( $category_selected->getMedia()->getLink(), 'category_thumb_api'),
                        "title"=> $data["title"],
                        "message"=>$data["message"],
                        "image"=> $data["image"],
                        "icon"=>$data["icon"]
                        );
                        print_r($message);

            $setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());            
            $key=$setting->getFirebasekey();

            $message_video = $this->send_notification(null, $message,$key); 
            
            $this->addFlash('success', 'Operation has been done successfully ');

        }
        return $this->render('AppBundle:Home:notif_category.html.twig',array(
          "form"=>$form->createView()
          ));
    }
   public function notifUrlAction(Request $request)
    {
    
        memory_get_peak_usage();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');

        $em=$this->getDoctrine()->getManager();

        $defaultData = array();
        $form = $this->createFormBuilder($defaultData)
            ->setMethod('GET')
            ->add('title', TextType::class)
            ->add('message', TextareaType::class)   
            ->add('language', 'entity', array('class' => 'AppBundle:Language'))   
            ->add('url', UrlType::class,array("label"=>"Url"))
            ->add('icon', UrlType::class,array("label"=>"Large Icon","required"=>false))
            ->add('image', UrlType::class,array("label"=>"Big Picture","required"=>false))
            ->add('send', SubmitType::class,array("label"=>"Send notification"))
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $data = $form->getData();
            $language_selected = $em->getRepository("AppBundle:Language")->find($data["language"]);

            $message = array(
                        "type"=>"link",
                        "id"=>strlen($data["url"]),
                        "link"=>$data["url"],
                        "title"=> $data["title"],
                        "message"=>$data["message"],
                        "language"=>$language_selected->getId(),
                        "image"=> $data["image"],
                        "icon"=>$data["icon"]
                        );
            $setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());            
            $key=$setting->getFirebasekey();
            $message_image = $this->send_notification(null, $message,$key); 
           
            $this->addFlash('success', 'Operation has been done successfully ');
          
        }
        return $this->render('AppBundle:Home:notif_url.html.twig',array(
            "form"=>$form->createView()
        ));
    }


    public function notifPostAction(Request $request)
    {
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $em=$this->getDoctrine()->getManager();
        $defaultData = array();
        $form = $this->createFormBuilder($defaultData)
            ->setMethod('GET')
            ->add('title', TextType::class)
            ->add('message', TextareaType::class)
            ->add('object', 'entity', array('class' => 'AppBundle:Post'))           
            ->add('icon', UrlType::class,array("label"=>"Large Icon","required"=>false))
            ->add('image', UrlType::class,array("label"=>"Big Picture","required"=>false))
            ->add('send', SubmitType::class,array("label"=>"Send notification"))
            ->getForm();
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $data = $form->getData();
            $languages="";
            $selected_post = $em->getRepository("AppBundle:Post")->find($data["object"]);

            foreach ($selected_post->getLanguages() as $key => $value) {
                $languages.="_".$value->getId();
            }
            $message = array(
                  "type"=> "post",
                  "id"=> $selected_post->getId(),
                  "language"=>trim($languages,"_"),
                  "title"=> $data["title"],
                  "message"=>$data["message"],
                  "image"=> $data["image"],
                  "icon"=>$data["icon"]
                );
            $setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());            
            $key=$setting->getFirebasekey();
            $message_image = $this->send_notification(null, $message,$key); 
            $this->addFlash('success', 'Operation has been done successfully ');
        }
        return $this->render('AppBundle:Home:notif_post.html.twig',array(
          "form"=>$form->createView()
          ));
    }

  public function notifUserPostAction(Request $request)
    {
        memory_get_peak_usage();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $post_id= $request->query->get("post_id");
        $em=$this->getDoctrine()->getManager();

        $defaultData = array();
        $form = $this->createFormBuilder($defaultData)
            ->setMethod('GET')
            ->add('title', TextType::class)
            ->add('object', HiddenType::class,array("attr"=>array("value"=>$post_id)))
            ->add('message', TextareaType::class)
            ->add('icon', UrlType::class,array("label"=>"Large Icon","required"=>false))
            ->add('image', UrlType::class,array("label"=>"Big Picture","required"=>false))
            ->add('send', SubmitType::class,array("label"=>"Send notification"))
            ->getForm();
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // data is an array with "name", "email", and "message" keys
            $data = $form->getData();
            $selected_post = $em->getRepository("AppBundle:Post")->find($data["object"]);

            $user= $selected_post->getUser();

            $devices= $em->getRepository('AppBundle:Device')->findAll();
             if ($user==null) {
                throw new NotFoundHttpException("Page not found");  
            }
            $tokens=array();

            $tokens[]=$user->getToken();
             
            $data = $form->getData();
            $selected_post = $em->getRepository("AppBundle:Post")->find($data["object"]);
            $message = array(
                  "type"=> "post",
                  "id"=> $selected_post->getId(),
                  "title"=> $data["title"],
                  "message"=>$data["message"],
                  "image"=> $data["image"],
                  "icon"=>$data["icon"]
                );
             $setting = $em->getRepository('AppBundle:Settings')->findOneBy(array());            
             $key=$setting->getFirebasekey();
             $message_image = $this->send_notificationToken($tokens, $message,$key); 
             $this->addFlash('success', 'Operation has been done successfully ');
             return $this->redirect($this->generateUrl('app_post_index'));
        }else{
           $video= $em->getRepository("AppBundle:Post")->find($post_id);
        }
        return $this->render('AppBundle:Home:notif_user_post.html.twig',array(
            "form"=>$form->createView()));
    }  
    public function settingsAction(Request $request) {
        $em = $this->getDoctrine()->getManager();
        $setting = $em->getRepository("AppBundle:Settings")->findOneBy(array(), array());
        $form = $this->createForm(new SettingsType(), $setting);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            if ($setting->getFile() != null) {
                $media = $setting->getMedia();

                $media->setFile($setting->getFile());
                $media->delete($this->container->getParameter('files_directory'));
                $media->upload($this->container->getParameter('files_directory'));
                $em->persist($media);
                $em->flush();
            }
            $em->persist($setting);
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
        }
        return $this->render("AppBundle:Home:settings.html.twig", array("setting" => $setting, "form" => $form->createView()));
    }

    public function privacypolicyAction() {
        $em = $this->getDoctrine()->getManager();
        $setting = $em->getRepository("AppBundle:Settings")->findOneBy(array(), array());
        return $this->render("AppBundle:Home:privacypolicy.html.twig", array("setting" => $setting));
    }
    public function indexAction(Request $request)
    {   

        $em=$this->getDoctrine()->getManager();
        $supports_count= $em->getRepository("AppBundle:Support")->count();
        $devices_count= $em->getRepository("AppBundle:Device")->count();
        $slides_count= $em->getRepository("AppBundle:Slide")->count();
        $post_count= $em->getRepository("AppBundle:Post")->countAll();
        $review_count= $em->getRepository("AppBundle:Post")->countReview();
        $count_views= $em->getRepository("AppBundle:Post")->countViews();
        $count_shares= $em->getRepository("AppBundle:Post")->countShares();

        $category_count= $em->getRepository("AppBundle:Category")->count();
        $comment_count= $em->getRepository("AppBundle:Comment")->count();
        $language_count= $em->getRepository("AppBundle:Language")->count();
        $version_count= $em->getRepository("AppBundle:Version")->count();
        $users_count= $em->getRepository("UserBundle:User")->count();





        return $this->render('AppBundle:Home:index.html.twig',array(
            
                "count_shares"=>$count_shares,
                "slides_count"=>$slides_count,
                "count_views"=>$count_views,
                "devices_count"=>$devices_count,
                "post_count"=>$post_count,
                "category_count"=>$category_count,
                "review_count"=>$review_count,
                "users_count"=>$users_count,
                "comment_count"=>$comment_count,
                "version_count"=>$version_count,
                "language_count"=>$language_count,
                "supports_count"=>$supports_count

        ));
    }
    public function api_latestAction(Request $request,$language,$token){
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $page=0;
        $nombre=20;
        $em=$this->getDoctrine()->getManager();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        
        $repository_article = $em->getRepository('AppBundle:Post');
        $query_article = $repository_article->createQueryBuilder('a')
            ->leftJoin('a.languages', 'l')
            ->where('l.id in ('.$language.')',"a.enabled = true")
            ->addOrderBy('a.created', 'DESC')
            ->addOrderBy('a.id', 'asc')
            ->setFirstResult($nombre*$page)
            ->setMaxResults($nombre)
            ->getQuery();
        $articles = $query_article->getResult();

        $repository_article = $em->getRepository('AppBundle:Category');



        $language_obj=  $em->getRepository("AppBundle:Language")->find($language);
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $categories=array();
        $categories_bjs =   $em->getRepository("AppBundle:Category")->findBy(array("language"=>$language_obj),array("position"=>"asc"));
        foreach ($categories_bjs as $key => $category) {
            $s["id"]=$category->getId();
            $s["title"]=$category->getTitle();
            $s["image"]=$imagineCacheManager->getBrowserPath( $category->getMedia()->getLink(), 'category_thumb_api');
            $categories[]=$s;
        }

        $repository_slide = $em->getRepository('AppBundle:Slide');

        $query_slide = $repository_slide->createQueryBuilder('a')
        ->leftJoin('a.language', 'l')
        ->where('l.id in ('.$language.')')
        ->addOrderBy('a.position','asc')
        ->addOrderBy('a.id', 'asc')
        ->getQuery();
        
        $slides =   $query_slide->getResult();



        $questions=array();
        $questions_objs =  $em->getRepository("AppBundle:Question")->findBy(array("language"=>$language_obj,"enabled"=>true,"featured"=>true));
        foreach ($questions_objs as $key => $question) {
            $s["id"]=$question->getId();
            $s["question"]=$question->getQuestion();
            $s["multichoice"]=$question->getMulti();
            $s["close"]=($question->getOpen()==true)?false:true;
            $s["featured"]=false;
            $choices=array();
            foreach ($question->getAnswers() as $key => $tag) {
                $c["id"]=$tag->getId();
                $c["choice"]=$tag->getAnswer();
                $c["value"]=$tag->getValue();
                $choices[]=$c;
            }
            $s["choices"]=$choices;
            $questions[]=$s;
        }

        return $this->render('AppBundle:Home:api_all.html.php',array("articles"=>$articles,"categories"=>$categories,"slides"=>$slides,"questions"=>$questions));
    }
    public function api_deviceAction($tkn,$token){
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $code="200";
        $message="";
        $errors=array();
        $em = $this->getDoctrine()->getManager();
        $d=$em->getRepository('AppBundle:Device')->findOneBy(array("token"=>$tkn));
        if ($d==null) {
            $device = new Device();
            $device->setToken($tkn);
            $em->persist($device);
            $em->flush();
            $message="Deivce added";
        }else{
            $message="Deivce Exist";
        }

        $error=array(
            "code"=>$code,
            "message"=>$message,
            "values"=>$errors
        );
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($error, 'json');
        return new Response($jsonContent);
    }
    public function api_tagsAction(Request $request,$page,$language,$token){
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();


        $nombre=10;
        

        $repository_tags = $em->getRepository('AppBundle:Tag');

        $query_tags = $repository_tags->createQueryBuilder('k')
            ->select("k.id,CONCAT(Substring(UPPER(k.name), 1, 1), Substring(LOWER(k.name), 2, Length(k.name)-1)) as name,COUNT(a.id) as articles")
            ->leftJoin('k.posts', 'a')        
            ->leftJoin('a.languages', 'l')  
            ->where('l.id in ('.$language.')',"a.enabled=true","k.name not like ''","k.name not like ' '")
            ->groupBy('k.id')
            ->orderBy('articles', 'DESC')
            ->setFirstResult($nombre*$page)
            ->setMaxResults($nombre)
            ->getQuery();
        $tags_list =   $query_tags->getResult();
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($tags_list, 'json');
        return new Response($jsonContent);
    }

}