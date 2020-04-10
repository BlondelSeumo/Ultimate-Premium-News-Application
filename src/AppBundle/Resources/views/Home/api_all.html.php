<?php 

function truncate($text, $length=38)
   {
      $trunc = (strlen($text)>$length)?true:false;
      if($trunc)
         return substr($text, 0, $length).'...';
      else
         return $text;
   }
$list_articles=array();

foreach ($articles as $key => $post) {
	$a = array();

	$a["id"]=$post->getId();
	$a["title"]=$post->getTitle();
	$a["type"]=$post->getType();
	$a["content"]=$post->getContent();
	$a["review"]=$post->getReview();
	$a["comment"]=$post->getComment();
	$a["comments"]=sizeof($post->getComments());
	$a["views"]=$post->getViews();
	$a["user"]=$post->getUser()->getName();
	$a["trusted"]=$post->getUser()->getTrusted();
	$a["userid"]=$post->getUser()->getId();
	$a["userimage"]=$post->getUser()->getImage();
	$a["thumbnail"]= $this['imagine']->filter($view['assets']->getUrl($post->getMedia()->getLink()), 'post_thumb_api');
	if ($post->getType()=="video") {
		$a["original"]= str_replace("/media/cache/resolve/post_image_api/", "/",$this['imagine']->filter($view['assets']->getUrl($post->getLocalvideo()->getLink()), 'post_image_api'));
	}
	if ($post->getType()=="youtube") {
		$a["video"]= $post->getVideo();
	}
	$a["created"]=$view['time']->diff($post->getCreated());
	$a["tags"]=$post->getTags();
	$a["shares"]=$post->getShares();
	$list_articles[]=$a;
}

$list_slide=array();
foreach ($slides as $key => $slide) {
	$s=null;
    $s["id"]=$slide->getId();
    $s["title"]=$slide->getTitle();
    $s["type"]=$slide->getType();
    $s["image"]=$this['imagine']->filter($view['assets']->getUrl($slide->getMedia()->getLink()), 'slide_thumb');
    if ($slide->getType()==3 && $slide->getPost()!=null)
    {

		$a = array();
		$a["id"]=$slide->getPost()->getId();
		$a["title"]=$slide->getPost()->getTitle();
		$a["type"]=$slide->getPost()->getType();
		$a["content"]=$slide->getPost()->getContent();
		$a["review"]=$slide->getPost()->getReview();
		$a["comment"]=$slide->getPost()->getComment();
		$a["comments"]=sizeof($slide->getPost()->getComments());
		$a["views"]=$slide->getPost()->getViews();
		$a["user"]=$slide->getPost()->getUser()->getName();
		$a["trusted"]=$slide->getPost()->getUser()->getTrusted();
		$a["userid"]=$slide->getPost()->getUser()->getId();
		$a["userimage"]=$slide->getPost()->getUser()->getImage();
		$a["thumbnail"]= $this['imagine']->filter($view['assets']->getUrl($slide->getPost()->getMedia()->getLink()), 'post_thumb_api');
		if ($slide->getPost()->getType()=="video") {
			$a["original"]= str_replace("/media/cache/resolve/post_image_api/", "/",$this['imagine']->filter($view['assets']->getUrl($slide->getPost()->getLocalvideo()->getLink()), 'post_image_api'));
		}
		if ($slide->getPost()->getType()=="youtube") {
			$a["video"]= $slide->getPost()->getVideo();
		}
		$a["created"]=$view['time']->diff($slide->getPost()->getCreated());
		$a["tags"]=$slide->getPost()->getTags();
		$a["shares"]=$slide->getPost()->getShares();
		$s["post"]=$a;

    }elseif($slide->getType()==1 && $slide->getCategory()!=null){
		$c["id"]=$slide->getCategory()->getId();
        $c["title"]=$slide->getCategory()->getTitle();
        $c["image"]=$this['imagine']->filter($view['assets']->getUrl($slide->getCategory()->getMedia()->getLink()), 'category_thumb_api');
		$s["category"]=$c;
	}elseif($slide->getType()==2 && $slide->getUrl()!=null){
	    $s["url"]=$slide->getUrl();
    }
    $list_slide[]=$s;
}

$data=array();
$data["slides"]=$list_slide;
$data["categories"]=$categories;
$data["questions"]=$questions;
$data["posts"]=$list_articles;

echo json_encode($data, JSON_UNESCAPED_UNICODE);
 ?>