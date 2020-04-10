<?php
namespace AppBundle\Form;

use Ivory\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;

class PostReviewType extends AbstractType {
  public function buildForm(FormBuilderInterface $builder, array $options) {
    $builder->add('title', "text", array("label" => "Title"));
    $builder->add('content', CKEditorType::class,
      array(
        'config_name' => 'user_config',
      )
    );
    $builder->add('comment', null, array("label" => "Enabled comments"));
    $builder->add('tags', null, array("label" => "Tags (Keywords)"));

    $builder->add("categories", 'entity',
      array(
        'class' => 'AppBundle:Category',
        'expanded' => true,
        "multiple" => "true",
        'by_reference' => false,
      )
    );
    $builder->add("languages", 'entity',
      array(
        'class' => 'AppBundle:Language',
        'expanded' => true,
        "multiple" => "true",
        'by_reference' => false,
      )
    );
    $builder->add('save', 'submit', array("label" => "REVIEW"));
  }
  public function getName() {
    return 'post';
  }
}
?>