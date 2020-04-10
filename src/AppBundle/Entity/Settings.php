<?php

namespace AppBundle\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Doctrine\Common\Collections\ArrayCollection;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use MediaBundle\Entity\Media;
/**
 * Settings
 *
 * @ORM\Table(name="settings_table")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\SettingsRepository")
 */
class Settings
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
     *
     * @ORM\Column(name="appname", type="string", length=255 , nullable = true)
     */
    private $appname;

    /**
     * @var string
     *
     * @ORM\Column(name="appdescription", type="text", nullable = true)
     */
    private $appdescription;

    /**
     * @var string
     *
     * @ORM\Column(name="googleplay", type="text", nullable = true)
     */
    private $googleplay;

    /**
     * @var string
     *
     * @ORM\Column(name="privacypolicy", type="text", nullable = true)
     */
    private $privacypolicy;

    /**
     * @var string
     *
     * @ORM\Column(name="firebasekey", type="string", length=255 , nullable = true)
     */
    private $firebasekey;

    /**
     * @Assert\File(mimeTypes={"image/jpeg","image/png" },maxSize="40M")
     */
    private $file;
     /**
     * @ORM\ManyToOne(targetEntity="MediaBundle\Entity\Media")
     * @ORM\JoinColumn(name="media_id", referencedColumnName="id")
     * @ORM\JoinColumn(nullable=false)
     */
    private $media;

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
     * Set appname
     *
     * @param string $appname
     * @return Settings
     */
    public function setAppname($appname)
    {
        $this->appname = $appname;

        return $this;
    }

    /**
     * Get appname
     *
     * @return string 
     */
    public function getAppname()
    {
        return $this->appname;
    }

    /**
     * Set appdescription
     *
     * @param string $appdescription
     * @return Settings
     */
    public function setAppdescription($appdescription)
    {
        $this->appdescription = $appdescription;

        return $this;
    }

    /**
     * Get appdescription
     *
     * @return string 
     */
    public function getAppdescription()
    {
        return $this->appdescription;
    }

    /**
     * Set googleplay
     *
     * @param string $googleplay
     * @return Settings
     */
    public function setGoogleplay($googleplay)
    {
        $this->googleplay = $googleplay;

        return $this;
    }

    /**
     * Get googleplay
     *
     * @return string 
     */
    public function getGoogleplay()
    {
        return $this->googleplay;
    }

    /**
     * Set privacypolicy
     *
     * @param string $privacypolicy
     * @return Settings
     */
    public function setPrivacypolicy($privacypolicy)
    {
        $this->privacypolicy = $privacypolicy;

        return $this;
    }

    /**
     * Get privacypolicy
     *
     * @return string 
     */
    public function getPrivacypolicy()
    {
        return $this->privacypolicy;
    }

    /**
     * Set firebasekey
     *
     * @param string $firebasekey
     * @return Settings
     */
    public function setFirebasekey($firebasekey)
    {
        $this->firebasekey = $firebasekey;

        return $this;
    }

    /**
     * Get firebasekey
     *
     * @return string 
     */
    public function getFirebasekey()
    {
        return $this->firebasekey;
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
     * Set media
     *
     * @param string $media
     * @return image
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
}
