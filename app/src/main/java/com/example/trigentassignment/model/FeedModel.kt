package com.example.trigentassignment.model

data class FeedModel(var imageHref:String, var title:String, var description:String,
                     var Header_title:String){


    override fun toString(): String {
        return "FeedModel(imageHref='$imageHref', title='$title', description='$description', Header_title='$Header_title')"
    }

}