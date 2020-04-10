<?php 

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


echo json_encode($a, JSON_UNESCAPED_UNICODE);?>