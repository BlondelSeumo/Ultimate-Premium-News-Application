<?php

namespace AppBundle\Controller;



use Doctrine\Common\Collections\ArrayCollection;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

use AppBundle\Entity\Question;
use AppBundle\Form\QuestionType;
use Symfony\Component\HttpFoundation\Response;

use Symfony\Component\HttpFoundation\Cookie;
use AppBundle\Entity\Answer;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Form\FormError;

class QuestionController extends Controller
{



    public function api_allAction(Request $request,$page,$language,$token){
        if ($token!=$this->container->getParameter('token_app')) {
            throw new NotFoundHttpException("Page not found");  
        }
        $nombre=20;
        $em=$this->getDoctrine()->getManager();

        $list=array();


        $repository = $em->getRepository('AppBundle:Question');
        $query = $repository->createQueryBuilder('Q')
        ->leftJoin('Q.language', 'l')
        ->where("Q.enabled = true")
        ->addOrderBy('Q.created', 'DESC')
        ->addOrderBy('Q.id', 'asc')
        ->setFirstResult($nombre*$page)
        ->setMaxResults($nombre)
        ->getQuery();
        $questions = $query->getResult();
        
        foreach ($questions as $key => $question) {
            $s["id"]=$question->getId();
            $s["question"]=$question->getQuestion();
            $s["multichoice"]=$question->getMulti();
            $s["featured"]=$question->getFeatured();
            $s["close"]=($question->getOpen())?false:true;

            $choices=array();
            foreach ($question->getAnswers() as $key => $answer) {
                $c["id"]=$answer->getId();
                $c["choice"]=$answer->getAnswer();
                $c["value"]=$answer->getValue();
                $choices[]=$c;
            }
            $s["choices"]=$choices;
            $list[]=$s;
        }
        header('Content-Type: application/json'); 
        $encoders = array(new XmlEncoder(), new JsonEncoder());
        $normalizers = array(new ObjectNormalizer());
        $serializer = new Serializer($normalizers, $encoders);
        $jsonContent=$serializer->serialize($list, 'json');
        return new Response($jsonContent);

    }
    public function api_addAction(Request $request,$question,$choices,$token)
    {
        $code="200";
        $message="";
        $errors=array();
        $em=$this->getDoctrine()->getManager();
        $question = $em->getRepository('AppBundle:Question')->find($question);
        if ($question==null) {
            $code="404";
            $message = "Error : This Questionnaire not found";
        }elseif(!$question->getEnabled()){
            $code="403";
            $message = "Error : This Questionnaire is Disabled";
        }elseif(!$question->getOpen()){
            $code="402";
            $message = "Error : This Questionnaire is Closed";
        }else{
            if ($question->getMulti()) {
                $list = explode("_", $choices);
                foreach ($list as $key => $value) {
                    $choice = $em->getRepository('AppBundle:Answer')->find($value);
                    if ($choice) {
                        if ($choice->getQuestion()->getId()==$question->getId()) {
                           $choice->setValue($choice->getValue()+1);          
                           $em->flush();
                        }
                    }
                    
                }
            }else{
                $choice = $em->getRepository('AppBundle:Answer')->find($choices);
                if ($choice) {
                    if ($choice->getQuestion()->getId()==$question->getId()) {
                        $choice->setValue($choice->getValue()+1);          
                        $em->flush();
                    }
                }
                
            }
            $code = "200";
            $message = "Thank your your vote has been registered";
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
    public function indexAction(Request $request)
    {
        $em=$this->getDoctrine()->getManager();
        $dql   =    "SELECT s FROM AppBundle:Question s";
        $query =    $em->createQuery($dql);
        $paginator  = $this->get('knp_paginator');
        $pagination = $paginator->paginate(
            $query, /* query NOT result */
            $request->query->getInt('page', 1)/*page number*/,
            12/*limit per page*/
        );
        $questions =   $em->getRepository("AppBundle:Question")->findBy(array(),array("created"=>"desc"));
        return $this->render('AppBundle:Question:index.html.twig',array("pagination"=>$pagination,"questions"=>$questions));
    }

    public function addAction(Request $request)
    {
        $Question = new Question();

        // dummy code - this is here just so that the Question has some answers
        // otherwise, this isn't an interesting example
        $answer1 = new Answer();
        $answer1->setAnswer('Answer 2');
        $Question->getAnswers()->add($answer1);
        $answer2 = new Answer();
        $answer2->setAnswer('Answer 1');
        $Question->getAnswers()->add($answer2);
        // end dummy code

        $form = $this->createForm(QuestionType::class, $Question);

        $form->handleRequest($request);
        foreach ($Question->getAnswers() as $answer) {
            if ($answer->getValue()==null) {
               $answer->setValue(0);
            }
         }
        if ($form->isSubmitted() && $form->isValid()) {
                    foreach ($Question->getAnswers() as $answer) {
                        if ($answer->getValue()==null) {
                           $answer->setValue(0);
                        }
                        if ($answer->getAnswer()!=null) {
                            $Question->addAnswer($answer);
                        }
                        
                    }
                    $em=$this->getDoctrine()->getManager();
                    $em->persist($Question);
                    $em->flush();
                    $this->addFlash('success', 'votre question bien supprimer');
                    return $this->redirect($this->generateUrl('app_question_index'));
        }
        return $this->render("AppBundle:Question:add.html.twig",array( 'form' => $form->createView()));
    }
    public function deleteAction($id,Request $request){
        $em=$this->getDoctrine()->getManager();
        $question = $em->getRepository("AppBundle:Question")->find($id);
        if($question==null){
            throw new NotFoundHttpException("Page not found");
        }
        $form=$this->createFormBuilder(array('id' => $id))
            ->add('id', 'hidden')
            ->add('Yes', 'submit')
            ->getForm();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()) {
            $em->remove($question);
            $em->flush();
            $this->addFlash('success', 'votre question bien supprimer');
            return $this->redirect($this->generateUrl('app_question_index'));
        }
        return $this->render('AppBundle:Question:delete.html.twig',array("form"=>$form->createView()));
    }

    public function editAction(Request $request,$id)
    {
        $em = $this->getDoctrine()->getManager();
        $question = $em->getRepository('AppBundle:Question')->find($id);

        if (!$question) {
            throw $this->createNotFoundException('No Question found for id '.$id);
        }

        $originalAnswers = new ArrayCollection();

        // Create an ArrayCollection of the current Answer objects in the database
        foreach ($question->getAnswers() as $answer) {
            $originalAnswers->add($answer);
            if ($answer->getValue()==null) {
               $answer->setValue(0);
            }
            if ($answer->getAnswer()!=null) {
                $originalAnswers->add($answer);
            }
                    
        }

        $form = $this->createForm(QuestionType::class, $question);

        $form->handleRequest($request);
        foreach ($question->getAnswers() as $answer) {
                if ($answer->getValue()==null) {
                   $answer->setValue(0);
                }
                if ($answer->getAnswer()==null) {
                    $answer->setAnswer("No Name");
                }
                    
        }
        if ($form->isValid()) {

            // remove the relationship between the answer and the Question
            foreach ($originalAnswers as $answer) {

                if (false === $question->getAnswers()->contains($answer)) {
                   // $answer->setQuestion(null);
                    $question->removeAnswer($answer);
                    $em->persist($answer);

                    // if you wanted to delete the Answer entirely, you can also do that
                    $em->remove($answer);
                }
            }
            foreach ($question->getAnswers() as $answer) {
                if ($answer->getValue()==null) {
                   $answer->setValue(0);
                }
                if ($answer->getAnswer()!=null) {
                    $question->addAnswer($answer);
                }
            }
            $em->persist($question);
            $em->flush();
            $this->addFlash('success', 'votre question bien enregistrer');
            return $this->redirect($this->generateUrl('app_question_index'));
        }
        return $this->render("AppBundle:Question:edit.html.twig",array("form"=>$form->createView()));
    }
    public function viewAction(Request $request)
    {
        $em=$this->getDoctrine()->getManager();
        $questions=$em->getRepository("AppBundle:Question")->findBy(array("enabled"=>true,'fixed'=>true));
        return $this->render("AppBundle:Question:view.html.twig",array("questions"=>$questions));
    }
    public function voteAction(Request $request)
    {
        if($request->isXmlHttpRequest()) {
            $id =$request->request->get('question');
            $em=$this->getDoctrine()->getManager();
            $question = $em->getRepository('AppBundle:Question')->find($id);
            if ($question->getMultichoice()==true) {
                foreach ($request->request->get('choice-'.$id) as $key => $value) {
                    $choice = $em->getRepository('AppBundle:Answer')->find($value);
                    $choice->setNombre($choice->getNombre()+1);          
                    $em->flush();
                }
                $response= new Response();
                $cookie = new Cookie('choice-'.$id,true);
                $response->headers->setCookie($cookie);
                $response->send();
            }else{
                $id_choice=$request->request->get('choice-'.$id);
                $choice = $em->getRepository('AppBundle:Answer')->find($id_choice);
                $choice->setNombre($choice->getNombre()+1);          
                $em->flush();
                $response= new Response();
                $cookie = new Cookie('choice-'.$id,true);
                $response->headers->setCookie($cookie);
                $response->send();
            }
        }
        return $this->render("AppBundle:Question:vote.html.twig",array("question"=>$question)); 
    }
}