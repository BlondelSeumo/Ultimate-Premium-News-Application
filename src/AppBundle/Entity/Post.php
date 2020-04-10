<?php

namespace AppBundle\Entity;

use Doctrine\ORM\Mapping as ORM;
use MediaBundle\Entity\Media;
use UserBundle\Entity\User;
use Symfony\Component\Validator\Constraints as Assert;
use Doctrine\Common\Collections\ArrayCollection;
/**
 * Post
 *
 * @ORM\Table(name="post_table")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\PostRepository")
 */
class Post
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
     *      min = 3
     * )
     * @ORM\Column(name="title", type="text")
     */
    private $title;


    /**
     * @var string
     * @ORM\Column(name="type", type="text")
     */
    private $type;

    /**
     * @var string
     * @Assert\NotBlank()
     * @Assert\Length(
     *      min = 15
     * )
     * @ORM\Column(name="content", type="text", nullable=true)
     */
    private $content;


    /**
     * @var int
     *
     * @ORM\Column(name="views", type="integer")
     */
    private $views;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="created", type="datetime")
     */
    private $created;

     /**
     * @ORM\ManyToOne(targetEntity="MediaBundle\Entity\Media")
     * @ORM\JoinColumn(name="media_id", referencedColumnName="id")
     * @ORM\JoinColumn(nullable=false)
     */
    private $media;

     /**
     * @ORM\ManyToOne(targetEntity="UserBundle\Entity\User")
     * @ORM\JoinColumn(name="user_id", referencedColumnName="id")
     * @ORM\JoinColumn(nullable=false)
     */
    private $user;
    /**
     * @var bool
     *
     * @ORM\Column(name="enabled", type="boolean")
     */
    private $enabled;

        /**
     * @var bool
     *
     * @ORM\Column(name="review", type="boolean")
     */
    private $review;

        /**
     * @ORM\ManyToMany(targetEntity="Category")
     * @ORM\JoinTable(name="post_categories",
     *      joinColumns={@ORM\JoinColumn(name="post_id", referencedColumnName="id",onDelete="CASCADE")},
     *      inverseJoinColumns={@ORM\JoinColumn(name="category_id", referencedColumnName="id",onDelete="CASCADE")},
     *      )
     */
    private $categories;


        /**
     * @ORM\ManyToMany(targetEntity="Language")
     * @ORM\JoinTable(name="post_languages",
     *      joinColumns={@ORM\JoinColumn(name="post_id", referencedColumnName="id",onDelete="CASCADE")},
     *      inverseJoinColumns={@ORM\JoinColumn(name="language_id", referencedColumnName="id",onDelete="CASCADE")},
     *      )
     */
    private $languages;

    /**
     * @Assert\File(mimeTypes={"image/jpeg","image/png" },maxSize="200M")
     */
    private $file;



    /**
    * @ORM\OneToMany(targetEntity="Comment", mappedBy="post",cascade={"persist", "remove"})
    * @ORM\OrderBy({"created" = "desc"})
    */
    private $comments;



    /**
     * @var bool
     *
     * @ORM\Column(name="comment", type="boolean")
     */
    private $comment;


    /**
     * @var string
     * @ORM\Column(name="tags", type="string", length=255,nullable=true)
     */
    private $tags;


        /**
     * @ORM\ManyToMany(targetEntity="Tag")
     * @ORM\JoinTable(name="posts_tags_table",
     *      joinColumns={@ORM\JoinColumn(name="post_id", referencedColumnName="id",onDelete="CASCADE")},
     *      inverseJoinColumns={@ORM\JoinColumn(name="tag_id", referencedColumnName="id",onDelete="CASCADE")},
     *      )
     */
    private $tagslist;


        /**
     * @var int
     *
     * @ORM\Column(name="shares", type="integer")
     */
    private $shares;

    /**
    * @ORM\OneToMany(targetEntity="Rate", mappedBy="post",cascade={"persist", "remove"})
    * @ORM\OrderBy({"created" = "desc"})
    */
    private $rates;


     /**
     * @ORM\ManyToOne(targetEntity="MediaBundle\Entity\Media")
     * @ORM\JoinColumn(name="localvideo_id", referencedColumnName="id")
     * @ORM\JoinColumn(nullable=true)
     */
    private $localvideo;

    /**
     * @Assert\File(mimeTypes={"video/mp4"},maxSize="512M")
     */
    private $videofile;

    /**
     * @var string
     * @Assert\Url()
     * @ORM\Column(name="video", type="string", length=255,nullable=true)
     */
    private $video;


    public function __construct()
    {
        $this->shares = 0 ;
        $this->views = 0 ;
        $this->categories = new ArrayCollection();
        $this->languages = new ArrayCollection();
        $this->comments = new ArrayCollection();
        $this->created= new \DateTime();
        $this->review = false;
    }

   /**
    * Get id
    * @return  
    */
    public function getId()
    {
        return $this->id;
    }
    
    /**
    * Set id
    * @return $this
    */
    public function setId($id)
    {
        $this->id = $id;
        return $this;
    }

    /**
     * Set title
     *
     * @param string $title
     * @return Wallpaper
     */
    public function setTitle($title)
    {
        $this->title = $title;

        return $this;
    }

    /**
     * Get title
     *
     * @return string 
     */
    public function getTitle()
    {
        return $this->title;
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
     * Set enabled
     *
     * @param boolean $enabled
     * @return Album
     */
    public function setEnabled($enabled)
    {
        $this->enabled = $enabled;

        return $this;
    }

    /**
     * Get enabled
     *
     * @return boolean 
     */
    public function getEnabled()
    {
        return $this->enabled;
    }
   
    /**
     * Set user
     *
     * @param string $user
     * @return Wallpaper
     */
    public function setUser(User $user)
    {
        $this->user = $user;

        return $this;
    }

    /**
     * Get user
     *
     * @return string 
     */
    public function getUser()
    {
        return $this->user;
    }
    /**
     * Set media
     *
     * @param string $media
     * @return Wallpaper
     */
    public function setMedia(Media $media)
    {
        $this->media = $media;

        return $this;
    }

    /**
     * Get media
     *
     * @return string 
     */
    public function getMedia()
    {
        return $this->media;
    }

   
          /**
     * Add categories
     *
     * @param Wallpaper $categories
     * @return Categorie
     */
    public function addCategory(Category $categories)
    {
        $this->categories[] = $categories;

        return $this;
    }

    /**
     * Remove categories
     *
     * @param Category $categories
     */
    public function removeCategory(Category $categories)
    {
        $this->categories->removeElement($categories);
    }

    /**
     * Get categories
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getCategories()
    {
        return $this->categories;
    }
        /**
     * Get categories
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function setCategories($categories)
    {
        return $this->categories =  $categories;
    }






     /**
     * Add colors
     *
     * @param Wallpaper $languages
     * @return Wallpaper
     */
    public function addLanguage(Language $language)
    {
        $this->languages[] = $language;

        return $this;
    }

    /**
     * Remove languages
     *
     * @param language $languages
     */
    public function removeLanguage(Language $language)
    {
        $this->languages->removeElement($language);
    }

    /**
     * Get languages
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getLanguages()
    {
        return $this->languages;
    }
        /**
     * Get colors
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function setLanguages($languages)
    {
        return $this->languages =  $languages;
    }
   
    public function getFile()
    {
        return $this->file;
    }
    public function setFile($file)
    {
        $this->file = $file;
        return $this;
    }
    /**
    * Get review
    * @return  
    */
    public function getReview()
    {
        return $this->review;
    }
    
    /**
    * Set review
    * @return $this
    */
    public function setReview($review)
    {
        $this->review = $review;
        return $this;
    }
    public function __toString()
    {
        return $this->getTitle();
    }
        /**
    * Get comment
    * @return  
    */
    public function getComment()
    {
        return $this->comment;
    }
    
    /**
    * Set comment
    * @return $this
    */
    public function setComment($comment)
    {
        $this->comment = $comment;
        return $this;
    }

       /**
     * Add comments
     *
     * @param Wallpaper $comments
     * @return Categorie
     */
    public function addComment(Comment $comments)
    {
        $this->comments[] = $comments;

        return $this;
    }

    /**
     * Remove comments
     *
     * @param Comment $comments
     */
    public function removeComment(Comment $comments)
    {
        $this->comments->removeElement($comments);
    }

    /**
     * Get comments
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getComments()
    {
        return $this->comments;
    }
    /**
    * Get tags
    * @return  
    */
    public function getTags()
    {
        return $this->tags;
    }
    
    /**
    * Set tags
    * @return $this
    */
    public function setTags($tags)
    {
        $this->tags = $tags;
        return $this;
    }
    /**
    * Get shares
    * @return  
    */
    public function getShares()
    {
        return $this->shares;
    }
    
    /**
    * Set shares
    * @return $this
    */
    public function setShares($shares)
    {
        $this->shares = $shares;
        return $this;
    }

    /**
    * Get content
    * @return  
    */
    public function getContent()
    {
        return str_replace('\n', "</br>", $this->content);
    }
    
    /**
    * Set content
    * @return $this
    */
    public function setContent($content)
    {
        $this->content = $content;
        return $this;
    }
    
    /**
    * Get views
    * @return  
    */
    public function getViews()
    {
        return $this->views;
    }
     /**
    * Get views
    * @return  
    */
    public function getViewsnumber()
    {
        return $this->number_format_short($this->views). " View(s)";
    }  

    /**
    * Set views
    * @return $this
    */
    public function setViews($views)
    {
        $this->views = $views;
        return $this;
    }

    /**
     * @param $n
     * @return string
     * Use to convert large positive numbers in to short form like 1K+, 100K+, 199K+, 1M+, 10M+, 1B+ etc
     */
    function number_format_short( $n ) {
        if ($n==0){
             return 0;
        }
        if ($n > 0 && $n < 1000) {
            // 1 - 999
            $n_format = floor($n);
            $suffix = '';
        } else if ($n >= 1000 && $n < 1000000) {
            // 1k-999k
            $n_format = floor($n / 1000);
            $suffix = 'K+';
        } else if ($n >= 1000000 && $n < 1000000000) {
            // 1m-999m
            $n_format = floor($n / 1000000);
            $suffix = 'M+';
        } else if ($n >= 1000000000 && $n < 1000000000000) {
            // 1b-999b
            $n_format = floor($n / 1000000000);
            $suffix = 'B+';
        } else if ($n >= 1000000000000) {
            // 1t+
            $n_format = floor($n / 1000000000000);
            $suffix = 'T+';
        }

        return !empty($n_format . $suffix) ? $n_format . $suffix : 0;
    }
          /**
     * Add tags
     *
     * @param Wallpaper $tags
     * @return tag
     */
    public function addTagslist(Tag $tagslist)
    {
        $this->tagslist[] = $tagslist;

        return $this;
    }

    /**
     * Remove tags
     *
     * @param tag $tags
     */
    public function removeTagslist(Tag $tagslist)
    {
        $this->tagslist->removeElement($tagslist);
    }

    /**
     * Get tags
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getTagslist()
    {
        return $this->tagslist;
    }
        /**
     * Get tags
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function setTagslist($tagslist)
    {
        return $this->tagslist =  $tagslist;
    }
    /**
    * Get type
    * @return  
    */
    public function getType()
    {
        return $this->type;
    }
    
    /**
    * Set type
    * @return $this
    */
    public function setType($type)
    {
        $this->type = $type;
        return $this;
    }
    /**
    * Get videofile
    * @return  
    */
    public function getVideofile()
    {
        return $this->videofile;
    }
    
    /**
    * Set videofile
    * @return $this
    */
    public function setVideofile($videofile)
    {
        $this->videofile = $videofile;
        return $this;
    }
    /**
    * Get video
    * @return  
    */
    public function getVideo()
    {
        return $this->video;
    }
    /**
    * Get video
    * @return  
    */
    public function getYoutube()
    {
        $parts = parse_url($this->getVideo());
        if(isset($parts['query'])){
            parse_str($parts['query'], $qs);
            if(isset($qs['v'])){
                return $qs['v'];
            }else if(isset($qs['vi'])){
                return $qs['vi'];
            }
        }
        if(isset($parts['path'])){
            $path = explode('/', trim($parts['path'], '/'));
            return $path[count($path)-1];
        }
        return false;
    }

    /**
    * Set video
    * @return $this
    */
    public function setVideo($video)
    {
        $this->video = $video;
        return $this;
    }
    /**
    * Get localvideo
    * @return  
    */
    public function getLocalvideo()
    {
        return $this->localvideo;
    }
    
    /**
    * Set localvideo
    * @return $this
    */
    public function setLocalvideo($localvideo)
    {
        $this->localvideo = $localvideo;
        return $this;
    }
}
