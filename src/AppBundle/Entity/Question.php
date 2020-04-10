<?php 
namespace AppBundle\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Language
 *
 * @ORM\Table(name="question_table")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\QuestionRepository")
 */
class Question
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
     *      max = 100,
     * )
     * @ORM\Column(name="question", type="string", length=255))
     */
    protected $question;

    /**
     * @var bool
     *
     * @ORM\Column(name="enabled", type="boolean")
     */
    private $enabled;


    /**
     * @var bool
     *
     * @ORM\Column(name="featured", type="boolean")
     */
    private $featured;

    /**
     * @var bool
     *
     * @ORM\Column(name="open", type="boolean")
     */
    private $open;


        /**
     * @var bool
     *
     * @ORM\Column(name="multi", type="boolean")
     */
    private $multi;

    /**
     * @ORM\OneToMany(targetEntity="Answer", mappedBy="question",cascade={"persist", "remove"})
     */
    protected $answers;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="created", type="datetime")
     */
    private $created;

     /**
     * @Assert\NotBlank()
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\Language")
     * @ORM\JoinColumn(name="language_id", referencedColumnName="id")
     * @ORM\JoinColumn(nullable=false)
     */
    private $language;


    public function __construct()
    {
        $this->answers = new ArrayCollection();
        $this->created= new \DateTime();

    }
    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }
    public function getQuestion()
    {
        return $this->question;
    }

    public function setQuestion($question)
    {
        $this->question = $question;
    }

    public function getAnswers()
    {
        return $this->answers;
    }
    public function addAnswer(Answer $answer)
    {
        $answer->addQuestion($this);

        $this->answers->add($answer);
    }
    public function removeAnswer(Answer $answer)
    {
        $this->answers->removeElement($answer);
    }
    /**
    * Get enabled
    * @return  
    */
    public function getEnabled()
    {
        return $this->enabled;
    }
    
    /**
    * Set enabled
    * @return $this
    */
    public function setEnabled($enabled)
    {
        $this->enabled = $enabled;
        return $this;
    }
    /**
    * Get featured
    * @return  
    */
    public function getFeatured()
    {
        return $this->featured;
    }
    
    /**
    * Set featured
    * @return $this
    */
    public function setFeatured($featured)
    {
        $this->featured = $featured;
        return $this;
    }
    /**
    * Get multi
    * @return  
    */
    public function getMulti()
    {
        return $this->multi;
    }
    
    /**
    * Set multi
    * @return $this
    */
    public function setMulti($multi)
    {
        $this->multi = $multi;
        return $this;
    }
    /**
     * Set created
     *
     * @param \DateTime $created
     * @return Wallpaper
     */
    public function setCreated($created)
    {
        $this->created = $created;

        return $this;
    }

    /**
     * Get created
     *
     * @return \DateTime 
     */
    public function getCreated()
    {
        return $this->created;
    }
    /**
    * Get open
    * @return  
    */
    public function getOpen()
    {
        return $this->open;
    }
    
    /**
    * Set open
    * @return $this
    */
    public function setOpen($open)
    {
        $this->open = $open;
        return $this;
    }
    /**
    * Get language
    * @return  
    */
    public function getLanguage()
    {
        return $this->language;
    }
    
    /**
    * Set language
    * @return $this
    */
    public function setLanguage(Language $language)
    {
        $this->language = $language;
        return $this;
    }
}