<?php 
namespace AppBundle\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use AppBundle\Entity\Language;
use MediaBundle\Entity\Media;
use AppBundle\Form\LanguageType;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Form\FormError;
class LanguageController extends Controller
{

    public function api_allAction(Request $request,$token)
    {
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $languages=$em->getRepository("AppBundle:Language")->findBy(array("enabled"=>true),array("position"=>"asc"));
        $list=array();
        foreach ($languages as $key => $language) {
            $s["id"]=$language->getId();
            $s["language"]=$language->getLanguage();
            $s["image"]=$imagineCacheManager->getBrowserPath( $language->getMedia()->getLink(), 'language_thumb_api');
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
        $imagineCacheManager = $this->get('liip_imagine.cache.manager');
        $guide=$em->getRepository("AppBundle:Post")->findOneBy(array("id"=>$id));
        $languages=$guide->getLanguages();
        $list=array();
        foreach ($languages as $key => $language) {
            $s["id"]=$language->getId();
            $s["language"]=$language->getLanguage();
            $s["image"]=$imagineCacheManager->getBrowserPath( $language->getMedia()->getLink(), 'language_thumb_api');
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
        $language= new Language();
        $form = $this->createForm(new LanguageType(),$language);
        $em=$this->getDoctrine()->getManager();
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            if( $language->getFile()!=null ){
                $media= new Media();
                $media->setFile($language->getFile());
                $media->upload($this->container->getParameter('files_directory'));
                $em->persist($media);
                $em->flush();
                $language->setMedia($media);
                $max=0;
                $languages=$em->getRepository('AppBundle:Language')->findAll();
                foreach ($languages as $key => $value) {
                    if ($value->getPosition()>$max) {
                        $max=$value->getPosition();
                    }
                }
                $language->setPosition($max+1);
                $em->persist($language);
                $em->flush();
                $this->addFlash('success', 'Operation has been done successfully');
                return $this->redirect($this->generateUrl('app_language_index'));
            }else{
                $error = new FormError("Required image file");
                $form->get('file')->addError($error);
            }
        }
        return $this->render("AppBundle:Language:add.html.twig",array("form"=>$form->createView()));
    }
    public function indexAction()
    {
	    $em=$this->getDoctrine()->getManager();
        $languages=$em->getRepository('AppBundle:Language')->findBy(array(),array("position"=>"asc"));
	    return $this->render('AppBundle:Language:index.html.twig',array("languages"=>$languages));    
	}

    public function upAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $language=$em->getRepository("AppBundle:Language")->find($id);
        if ($language==null) {
            throw new NotFoundHttpException("Page not found");
        }
        if ($language->getPosition()>1) {
            $p=$language->getPosition();
            $languages=$em->getRepository('AppBundle:Language')->findAll();
            foreach ($languages as $key => $value) {
                if ($value->getPosition()==$p-1) {
                    $value->setPosition($p);  
                }
            }
            $language->setPosition($language->getPosition()-1);
            $em->flush(); 
        }
        return $this->redirect($this->generateUrl('app_language_index'));
    }
    public function downAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $language=$em->getRepository("AppBundle:Language")->find($id);
        if ($language==null) {
            throw new NotFoundHttpException("Page not found");
        }
        $max=0;
        $languages=$em->getRepository('AppBundle:Language')->findBy(array(),array("position"=>"asc"));
        foreach ($languages  as $key => $value) {
            $max=$value->getPosition();  
        }
        if ($language->getPosition()<$max) {
            $p=$language->getPosition();
            foreach ($languages as $key => $value) {
                if ($value->getPosition()==$p+1) {
                    $value->setPosition($p);  
                }
            }
            $language->setPosition($language->getPosition()+1);
            $em->flush();  
        }
        return $this->redirect($this->generateUrl('app_language_index'));
    }
    public function deleteAction($id,Request $request){
        $em=$this->getDoctrine()->getManager();

        $language = $em->getRepository("AppBundle:Language")->find($id);
        if($language==null){
            throw new NotFoundHttpException("Page not found");
        }

        $form=$this->createFormBuilder(array('id' => $id))
            ->add('id', 'hidden')
            ->add('Yes', 'submit')
            ->getForm();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()) {
                $media_old = $language->getMedia();
                $em->remove($language);
                $em->flush();
                if( $media_old!=null ){
                    $media_old->delete($this->container->getParameter('files_directory'));
                    $em->remove($media_old);
                    $em->flush();
                }
                $languages=$em->getRepository('AppBundle:Language')->findBy(array(),array("position"=>"asc"));

                $p=1;
                foreach ($languages as $key => $value) {
                    $value->setPosition($p); 
                    $p++; 
                }
                $em->flush();

                $this->addFlash('success', 'Operation has been done successfully');

            return $this->redirect($this->generateUrl('app_language_index'));
        }
        return $this->render('AppBundle:Language:delete.html.twig',array("form"=>$form->createView()));
    }
    public function editAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $language=$em->getRepository("AppBundle:Language")->find($id);
        if ($language==null) {
            throw new NotFoundHttpException("Page not found");
        }
        $form = $this->createForm(new LanguageType(),$language);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
             if( $language->getFile()!=null ){
                $media= new Media();
                $media_old=$language->getMedia();
                $media->setFile($language->getFile());
                $media->upload($this->container->getParameter('files_directory'));
                $em->persist($media);
                $em->flush();
                $language->setMedia($media);
                $em->flush();
                $media_old->delete($this->container->getParameter('files_directory'));
                $em->remove($media_old);
                $em->flush();
            }
            $em->persist($language);
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
            return $this->redirect($this->generateUrl('app_language_index'));
        }
        return $this->render("AppBundle:Language:edit.html.twig",array("form"=>$form->createView()));
    }

}
?>