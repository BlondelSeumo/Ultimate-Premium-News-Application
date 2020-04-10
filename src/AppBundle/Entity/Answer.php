<?php 
namespace AppBundle\Entity;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Slide
 *
 * @ORM\Table(name="answers_table")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\AnswerRepository")
 */
class Answer
{
    /**
     * @var int
     *
     * @ORM\Column(name="id", type="integer")
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    private $id;
    /**
     * @var string
     * @Assert\NotBlank()
     * @Assert\Length(
     *      min = 1,
     *      max = 30,
     * )
     * @ORM\Column(name="answer", type="string", length=255))
     */
    private $answer;



    /**
     * @var int
     * @Assert\NotBlank()
    * @Assert\Range(
     *      min = 0
     * )
     * @ORM\Column(name="value", type="integer")
     */
    private $value;

    /**
     * @ORM\ManyToOne(targetEntity="Question", inversedBy="answers")
     * @ORM\JoinColumn(name="question_id", referencedColumnName="id")
     */
    private $question;
    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    public function getAnswer()
    {
        return $this->answer;
    }

    public function setAnswer($answer)
    {
        $this->answer = $answer;
    }
    /**
    * Get value
    * @return  
    */
    public function getValue()
    {
        return $this->value;
    }
    
    /**
    * Set value
    * @return $this
    */
    public function setValue($value)
    {
        $this->value = $value;
        return $this;
    }
    public function addQuestion(Question $question)
    {
        $this->question=$question;
    }
    public function getQuestion()
    {
       return $this->question;
    }
}