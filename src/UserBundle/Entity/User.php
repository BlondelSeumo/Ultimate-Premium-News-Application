<?php
// src/AppBundle/Entity/User.php

namespace UserBundle\Entity;

use FOS\UserBundle\Entity\User as BaseUser;
use Doctrine\ORM\Mapping as ORM;
use MediaBundle\Entity\Media as Media;
use Doctrine\Common\Collections\ArrayCollection;

/**
 * @ORM\Entity
 * @ORM\Table(name="fos_user_table")
* @ORM\Entity(repositoryClass="UserBundle\Repository\UserRepository")
 */
class User extends BaseUser
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $id;
    /** 
    @ORM\Column(name="name", type="string", length=255, nullable=true) 
    */
    protected $name; 

    /** 
    @ORM\Column(name="code", type="string", length=255, nullable=true) 
    */
    protected $code; 

    /** 
    @ORM\Column(name="facebook", type="string", length=255, nullable=true) 
    */
    protected $facebook; 

        /** 
    @ORM\Column(name="instagram", type="string", length=255, nullable=true) 
    */
    protected $instagram; 

            /** 
    @ORM\Column(name="twitter", type="string", length=255, nullable=true) 
    */
    protected $twitter; 

    /** 
    @ORM\Column(name="emailo", type="string", length=255, nullable=true) 
    */
    protected $emailo; 

    /** 
    @ORM\Column(name="type", type="string", length=255, nullable=true) 
    */
    protected $type; 

    /** 
    @ORM\Column(name="token", type="text", nullable=true) 
    */
    protected $token; 

    /** 
    @ORM\Column(name="image", type="text") 
    */
    private $image;

        /**
     * @ORM\ManyToMany(targetEntity="User")
     * @ORM\JoinTable(name="user_followers",
     *      joinColumns={@ORM\JoinColumn(name="user_id", referencedColumnName="id",onDelete="CASCADE")},
     *      inverseJoinColumns={@ORM\JoinColumn(name="follower_id", referencedColumnName="id",onDelete="CASCADE")},
     *      )
     */
    private $followers;

    /**
    * @ORM\OneToMany(targetEntity="AppBundle\Entity\Post", mappedBy="user")
    * @ORM\OrderBy({"created" = "asc"})
     */
    private $posts;


    /**
     * @ORM\ManyToMany(targetEntity="User"  ,mappedBy="followers")
     */
    private $users;

        /**
    * @ORM\OneToMany(targetEntity="AppBundle\Entity\Rate", mappedBy="user",cascade={"persist", "remove"})
    * @ORM\OrderBy({"created" = "desc"})
     */
    private $ratings;

        /**
    * @ORM\OneToMany(targetEntity="AppBundle\Entity\Comment", mappedBy="user",cascade={"persist", "remove"})
    * @ORM\OrderBy({"created" = "desc"})
     */
    private $comments;

    /**
     * @var bool
     *
     * @ORM\Column(name="trusted", type="boolean")
     */
    private $trusted;

    public function __construct()
    {
        parent::__construct();
        $this->ratings = new ArrayCollection();
        $this->comments = new ArrayCollection();
        $this->followers = new ArrayCollection();
        $this->followers = new ArrayCollection();
        $this->users = new ArrayCollection();
        $this->trusted = false;
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
    * Get name
    * @return  
    */
    public function getName()
    {
        return ucfirst($this->name);
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

    public function setEmail($email) 
    {
        $this->email = $email;
        $this->username = $email;
    }
    public function __toString()
    {
       return $this->getName();
    }
    /**
    * Get image
    * @return  
    */
    public function getImage()
    {
        return $this->image;
    }
    
    /**
    * Set image
    * @return $this
    */
    public function setImage($image)
    {
        $this->image = $image;
        return $this;
    }
        /**
     * Add followers
     *
     * @param Wallpaper $followers
     * @return Categorie
     */
    public function addFollower(User $follower)
    {
        $this->followers[] = $follower;

        return $this;
    }

    /**
     * Remove followers
     *
     * @param Follower $followers
     */
    public function removeFollower(User $follower)
    {
        $this->followers->removeElement($follower);
    }

    /**
     * Get followers
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getFollowers()
    {
        return $this->followers;
    }
        /**
     * Get followers
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function setFollowers($followers)
    {
        return $this->followers =  $followers;
    }

    /**
     * Add User
     *
     * @param Wallpaper $users
     * @return Categorie
     */
    public function addUser(User $user)
    {
        $this->users[] = $user;

        return $this;
    }

    /**
     * Remove User
     *
     * @param User $users
     */
    public function removeUser(User $user)
    {
        $this->users->removeElement($user);
    }

    /**
     * Get users
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function getUsers()
    {
        return $this->users;
    }
        /**
     * Get users
     *
     * @return \Doctrine\Common\Collections\Collection 
     */
    public function setUsers($users)
    {
        return $this->users =  $users;
    }
    /**
    * Get facebook
    * @return  
    */
    public function getFacebook()
    {
        return $this->facebook;
    }
    
    /**
    * Set facebook
    * @return $this
    */
    public function setFacebook($facebook)
    {
        $this->facebook = $facebook;
        return $this;
    }
    /**
    * Get twitter
    * @return  
    */
    public function getTwitter()
    {
        return $this->twitter;
    }
    
    /**
    * Set twitter
    * @return $this
    */
    public function setTwitter($twitter)
    {
        $this->twitter = $twitter;
        return $this;
    }
    /**
    * Get instagram
    * @return  
    */
    public function getInstagram()
    {
        return $this->instagram;
    }
    
    /**
    * Set instagram
    * @return $this
    */
    public function setInstagram($instagram)
    {
        $this->instagram = $instagram;
        return $this;
    }
    /**
    * Get emailo
    * @return  
    */
    public function getEmailo()
    {
        return $this->emailo;
    }
    
    /**
    * Set emailo
    * @return $this
    */
    public function setEmailo($emailo)
    {
        $this->emailo = $emailo;
        return $this;
    }
    /**
    * Get token
    * @return  
    */
    public function getToken()
    {
        return $this->token;
    }
    
    /**
    * Set token
    * @return $this
    */
    public function setToken($token)
    {
        $this->token = $token;
        return $this;
    }
    /**
    * Get posts
    * @return  
    */
    public function getPosts()
    {
        return $this->posts;
    }

    /**
    * Set posts
    * @return $this
    */
    public function setPosts($posts)
    {
        $this->posts = $posts;
        return $this;
    }
    /**
    * Get code
    * @return  
    */
    public function getCode()
    {
        return $this->code;
    }
    
    /**
    * Set code
    * @return $this
    */
    public function setCode($code)
    {
        $this->code = $code;
        return $this;
    }
    /**
    * Get trusted
    * @return  
    */
    public function getTrusted()
    {
        return $this->trusted;
    }
    
    /**
    * Set trusted
    * @return $this
    */
    public function setTrusted($trusted)
    {
        $this->trusted = $trusted;
        return $this;
    }
    /**
    * Get comments
    * @return  
    */
    public function getComments()
    {
        return $this->comments;
    }
    
    /**
    * Set comments
    * @return $this
    */
    public function setComments($comments)
    {
        $this->comments = $comments;
        return $this;
    }
    /**
    * Get ratings
    * @return  
    */
    public function getRatings()
    {
        return $this->ratings;
    }
    
    /**
    * Set ratings
    * @return $this
    */
    public function setRatings($ratings)
    {
        $this->ratings = $ratings;
        return $this;
    }
}