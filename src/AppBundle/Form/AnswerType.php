<?php 
namespace AppBundle\Form;

use AppBundle\Entity\Answer;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AnswerType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder->add('answer',null,array("attr"=>array("required"=>true,    'empty_data' => 'John Doe',"placeholder"=>"Answer")));
        $builder->add('value',null,array("attr"=>array("placeholder"=>"Initial Vote number",'min' => 0)));
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => Answer::class,
        ));
    }
}