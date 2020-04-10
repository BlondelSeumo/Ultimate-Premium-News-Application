<?php
namespace AppBundle\Form;

use Ivory\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;

class PostType extends AbstractType {
	public function buildForm(FormBuilderInterface $builder, array $options) {
		$builder->add('title', "text", array("label" => "Title"));
		$builder->add('content', CKEditorType::class,
			array(
				'config_name' => 'user_config',
			)
		);
		$builder->add('enabled', null, array("label" => "Enabled"));
		$builder->add('comment', null, array("label" => "Enabled comments"));
		$builder->add('tags', null, array("label" => "Tags (Keywords)"));

		$builder->add("categories", 'entity',
			array(
				'class' => 'AppBundle:Category',
				'expanded' => true,
				"multiple" => "true",
				'by_reference' => false
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
		$builder->addEventListener(FormEvents::PRE_SET_DATA, function (FormEvent $event) {
			$article = $event->getData();
			$form = $event->getForm();
			if ($article and null !== $article->getId()) {
				$form->add("file", null, array("label" => "", "required" => false));
				$form->add('save', 'submit', array("label" => "SAVE"));

			} else {
				$form->add("file", null, array("label" => "", "required" => true));
				$form->add('save', 'submit', array("label" => "ADD"));
			}
		});
	}
	public function getName() {
		return 'guide';
	}
}
?>