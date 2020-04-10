<?php 
namespace AppBundle\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use AppBundle\Entity\Category;
use MediaBundle\Entity\Media;
use AppBundle\Form\CategoryType;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
class CategoryController extends Controller
{
    public function indexAction()
    {
        $em = $this->getDoctrine()->getManager();
        $categories =   $em->getRepository("AppBundle:Category")->findBy(array(),array("position"=>"asc"));
        return $this->render("AppBundle:Category:index.html.twig",array("categories"=>$categories));
    }
    public function api_popularAction(Request $request,$token,$language)
    {
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $list=array();

       $repository = $em->getRepository('AppBundle:Category');

        $query = $repository->createQueryBuilder('C')
          ->select(array("C.id","C.title","m.url as image","m.extension as extension","SUM(w.views) as test"))
          ->leftJoin('C.posts', 'w')
          ->leftJoin('C.media', 'm')
          ->groupBy('C.id')
          ->orderBy('test',"DESC")
          ->where('w.enabled=true',"C.language = ".$language)
          ->getQuery();

        $categories = $query->getResult();

        foreach ($categories as $key => $category) {
            $s["id"]=$category["id"];
            $s["title"]=$category["title"];
            $media =  new Media();
            $s["image"]=$imagineCacheManager->getBrowserPath("uploads/".$category["extension"]."/".$category["image"], 'category_thumb_api');
            $list[]=$s;
        }
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($list, 'json');
        return new Response($jsonContent);
    }
    public function api_allAction(Request $request,$token,$language)
    {
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();
        $languageObj=$em->getRepository("AppBundle:Language")->find($language);

        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $list=array();
        $categories =   $em->getRepository("AppBundle:Category")->findBy(array("language"=>$languageObj),array("position"=>"asc"));
        foreach ($categories as $key => $category) {
            $s["id"]=$category->getId();
            $s["title"]=$category->getTitle();
            $s["image"]=$imagineCacheManager->getBrowserPath( $category->getMedia()->getLink(), 'category_thumb_api');
            $list[]=$s;
        }
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($list, 'json');
        return new Response($jsonContent);
    }
    public function api_by_postAction(Request $request,$id,$token)
    {
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();
        $post=$em->getRepository("AppBundle:Post")->find($id);

        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $list=array();
        $categories =   $post->getCategories();
        foreach ($categories as $key => $category) {
            $s["id"]=$category->getId();
            $s["title"]=$category->getTitle();
            $s["image"]=$imagineCacheManager->getBrowserPath( $category->getMedia()->getLink(), 'category_thumb_api');
            $list[]=$s;
        }
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($list, 'json');
        return new Response($jsonContent);
    }
    
    public function addAction(Request $request)
    {
        $category= new Category();
        $form = $this->createForm(new CategoryType(),$category);
        $em=$this->getDoctrine()->getManager();
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
                if( $category->getFile()!=null ){
                    $media= new Media();
                    $media->setFile($category->getFile());
                    $media->upload($this->container->getParameter('files_directory'));
                    $em->persist($media);
                    $em->flush();
                    $category->setMedia($media);
                    $max=0;
                    $categories=$em->getRepository('AppBundle:Category')->findAll();
                    foreach ($categories as $key => $value) {
                        if ($value->getPosition()>$max) {
                            $max=$value->getPosition();
                        }
                    }
                    $category->setPosition($max+1);
                    $em->persist($category);
                    $em->flush();
                    $this->addFlash('success', 'Operation has been done successfully');
                    return $this->redirect($this->generateUrl('app_category_index'));
                }else{
                    $error = new FormError("Required image file");
                    $form->get('file')->addError($error);
                }
       }
        return $this->render("AppBundle:Category:add.html.twig",array("form"=>$form->createView()));
    }

    public function upAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $category=$em->getRepository("AppBundle:Category")->find($id);
        if ($category==null) {
            throw new NotFoundHttpException("Page not found");
        }
        if ($category->getPosition()>1) {
            $p=$category->getPosition();
            $categories=$em->getRepository('AppBundle:Category')->findAll();
            foreach ($categories as $key => $value) {
                if ($value->getPosition()==$p-1) {
                    $value->setPosition($p);  
                }
            }
            $category->setPosition($category->getPosition()-1);
            $em->flush(); 
        }
        return $this->redirect($this->generateUrl('app_category_index'));
    }
    public function downAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $category=$em->getRepository("AppBundle:Category")->find($id);
        if ($category==null) {
            throw new NotFoundHttpException("Page not found");
        }
        $max=0;
        $categories=$em->getRepository('AppBundle:Category')->findBy(array(),array("position"=>"asc"));
        foreach ($categories  as $key => $value) {
            $max=$value->getPosition();  
        }
        if ($category->getPosition()<$max) {
            $p=$category->getPosition();
            foreach ($categories as $key => $value) {
                if ($value->getPosition()==$p+1) {
                    $value->setPosition($p);  
                }
            }
            $category->setPosition($category->getPosition()+1);
            $em->flush();  
        }
        return $this->redirect($this->generateUrl('app_category_index'));
    }
    public function deleteAction($id,Request $request){
        $em=$this->getDoctrine()->getManager();

        $category = $em->getRepository("AppBundle:Category")->find($id);
        if($category==null){
            throw new NotFoundHttpException("Page not found");
        }

        $form=$this->createFormBuilder(array('id' => $id))
            ->add('id', 'hidden')
            ->add('Yes', 'submit')
            ->getForm();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()) {
            $media_old = $category->getMedia();
            $em->remove($category);
            $em->flush();
            if( $media_old!=null ){
                $media_old->delete($this->container->getParameter('files_directory'));
                $em->remove($media_old);
                $em->flush();
            }
            $categories=$em->getRepository('AppBundle:Category')->findBy(array(),array("position"=>"asc"));

            $p=1;
            foreach ($categories as $key => $value) {
                $value->setPosition($p); 
                $p++; 
            }
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
            return $this->redirect($this->generateUrl('app_category_index'));
        }
        return $this->render('AppBundle:Category:delete.html.twig',array("form"=>$form->createView()));
    }
    public function editAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $category=$em->getRepository("AppBundle:Category")->find($id);
        if ($category==null) {
            throw new NotFoundHttpException("Page not found");
        }
        $form = $this->createForm(new CategoryType(),$category);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            if( $category->getFile()!=null ){
                $media= new Media();
                $media_old=$category->getMedia();
                $media->setFile($category->getFile());
                $media->upload($this->container->getParameter('files_directory'));
                $em->persist($media);
                $em->flush();
                $category->setMedia($media);

                $media_old->delete($this->container->getParameter('files_directory'));
                $em->remove($media_old);
                $em->flush();
            }
            $em->persist($category);
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
            return $this->redirect($this->generateUrl('app_category_index'));
 
        }
        return $this->render("AppBundle:Category:edit.html.twig",array("category"=>$category,"form"=>$form->createView()));
    }
}
?>