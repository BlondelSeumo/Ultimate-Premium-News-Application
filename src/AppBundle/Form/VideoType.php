<?php
namespace AppBundle\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Ivory\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use AppBundle\Repository\CategoryRepository;

class VideoType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {$builder->add('title',null,array("label"=>"Title"));
       $builder->add('enabled',null,array("label"=>"Enabled"));
       $builder->add('comment',null,array("label"=>"Enabled comments"));
       $builder->add('video',null,array("required"=>true));

       $builder->add('languages',null,array("label"=>"language"));
       $builder->add('tags',null,array("label"=>"Tags"));
       $builder->add('content', CKEditorType::class, 
                    array(
                        'config_name' => 'user_config'
                    )
                );
        $builder->add(
                  "categories", 
                  "entity",
                  array('class' => 'AppBundle:Category',
                      "required"=>false,
                      "multiple" => "true",
                      'by_reference' => false,
                      'expanded' => true,
              ));
        $builder->add(
                  "languages", 
                  "entity",
                  array('class' => 'AppBundle:Language',
                      "required"=>false,
                      "multiple" => "true",
                      'by_reference' => false,
                      'expanded' => true,
              ));
      
      $builder->addEventListener(FormEvents::PRE_SET_DATA, function (FormEvent $event) {
            $article = $event->getData();
            $form = $event->getForm();
            if ($article and null !== $article->getId()) {
                 $form->add("file",null,array("label"=>"","required"=>false));
            }else{
                 $form->add("file",null,array("label"=>"","required"=>true));
            }
      });
      $builder->add('save', 'submit',array("label"=>"save"));
    }
    public function getName()
    {
        return 'guide';
    }
}
?>