<?php 
namespace AppBundle\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use AppBundle\Entity\Version;
use AppBundle\Form\VersionType;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
class VersionController extends Controller
{

    public function addAction(Request $request)
    {
        $version= new Version();
        $form = $this->createForm(new VersionType(),$version);
        $em=$this->getDoctrine()->getManager();
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($version);
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
            return $this->redirect($this->generateUrl('app_version_index'));
        }
        return $this->render("AppBundle:Version:add.html.twig",array("form"=>$form->createView()));
    }

    public function api_checkAction(Request $request,$code,$token)
    {

        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $em=$this->getDoctrine()->getManager();
        $version =   $em->getRepository("AppBundle:Version")->findOneBy(array("code"=>$code,"enabled"=>true));
        $response=array();
        $code="200";
        $message="";
        $errors=array();
        if ($version==null) {
            $versions =  $em->getRepository("AppBundle:Version")->findBy(array("enabled"=>true),array("code"=>"asc"));
            $a=null;
            foreach ($versions as $key => $value) {
                $a=$value;
            }
            if ($a==null) {
                $code="200";
                $response["name"]="update";
                $response["value"]="App on update";
            }else{
                $code="202";
                $response["name"]="update";
                $response["value"]="New version available ".$a->getTitle() ." please update your application";
                $message = $a->getFeatures();
            }
        }else{
            $code="200";
            $response["name"]="update";
            $response["value"]="App on update";
        }
        $errors[]=$response;
        $error=array(
                "code"=>$code,
                "message"=>$message,
                "values"=>$errors,
                );
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($error, 'json');
        return new Response($jsonContent);  
    }
    public function indexAction()
    {
	    $em=$this->getDoctrine()->getManager();
        $versions=$em->getRepository('AppBundle:Version')->findBy(array(),array("code"=>"asc"));
	    return $this->render('AppBundle:Version:index.html.twig',array("versions"=>$versions));    
	}
  

    public function deleteAction($id,Request $request){
        $em=$this->getDoctrine()->getManager();

        $version = $em->getRepository("AppBundle:Version")->find($id);
        if($version==null){
            throw new NotFoundHttpException("Page not found");
        }

        $form=$this->createFormBuilder(array('id' => $id))
            ->add('id', 'hidden')
            ->add('Yes', 'submit')
            ->getForm();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()) {
            
            //if (sizeof($version->getAlbums())==0) {
                $em->remove($version);
                $em->flush();


                $this->addFlash('success', 'Operation has been done successfully');
            //}else{
             //   $this->addFlash('danger', 'Operation has been cancelled ,Your album not empty');   
            //}
            return $this->redirect($this->generateUrl('app_version_index'));
        }
        return $this->render('AppBundle:Version:delete.html.twig',array("form"=>$form->createView()));
    }
    public function editAction(Request $request,$id)
    {
        $em=$this->getDoctrine()->getManager();
        $version=$em->getRepository("AppBundle:Version")->find($id);
        if ($version==null) {
            throw new NotFoundHttpException("Page not found");
        }
        $form = $this->createForm(new VersionType(),$version);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($version);
            $em->flush();
            $this->addFlash('success', 'Operation has been done successfully');
            return $this->redirect($this->generateUrl('app_version_index'));
 
        }
        return $this->render("AppBundle:Version:edit.html.twig",array("form"=>$form->createView()));
    }
}
?>