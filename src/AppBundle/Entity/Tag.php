<?php 
namespace AppBundle\Entity;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Slide
 *
 * @ORM\Table(name="tags_table")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\TagRepository")
 */
class Tag
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
     * @ORM\Column(name="name", type="string", length=255))
     */
    private $name;

    /**
     * @ORM\ManyToMany(targetEntity="Post"  ,mappedBy="tagslist")
     * @ORM\OrderBy({"created" = "desc"})
     */
    private $posts;



    /**
     * @var int
     *
     * @ORM\Column(name="search", type="integer")
     */
    private $search;

    public function __construct()
    {
        $this->search = 0;
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
    /**
    * Get name
    * @return  
    */
    public function getName()
    {
        return $this->name;
    }
    
    /**
    * Set name
    * @return $this
    */
    public function setName($name)
    {
        $this->name = $name;
        return $this;
    }
        /**
     * Add posts
     *
     * @param post $posts
     * @return Categorie
     */
    public function addPost(Post $post)
    {
        $this->posts[] = $post;

        return $this;
    }

    /**
     * Remove posts
     *
     * @param post $posts
     */
    public function removePost(Post $post)
    {
        $this->posts->removeElement($post);
    }

    /**
     * getposts
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getPosts()
    {
        return $this->posts;
    }
    /**
    * Get search
    * @return  
    */
    public function getSearch()
    {
        return $this->search;
    }
    
    /**
    * Set search
    * @return $this
    */
    public function setSearch($search)
    {
        $this->search = $search;
        return $this;
    }
}