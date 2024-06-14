import React from "react";
import TrendingCard from "./TrendingCard";

const trendingPosts = [
    {
        "text": "Trending post",
        "username": "Lautaro",
        "likes": 900,
    },
    {
        "text": "Trending post",
        "username": "Lautaro",
        "likes": 1111,
    },
    {
        "text": " post",
        "username": "Lautaro",
        "likes": 9003,
    },
    {
        "text": "un post de mierda",
        "username": "Lautaro",
        "likes": 213,
    },
    {
        "text": "otro post",
        "username": "Lautaro",
        "likes": 222,
    },
    {
        "text": "posteando el post",
        "username": "Lautaro",
        "likes": 900,
    },
    {
        "text": "postingposting post",
        "username": "Lautaro",
        "likes": 900,
    },
    {
        "text": "Trending post",
        "username": "Lautaro",
        "likes": 1,
    },
    {
        "text": "Trending post",
        "username": "Lautaro",
        "likes": 244,
    },
    {
        "text": "Trending post",
        "username": "Lautaro",
        "likes": 900,
    }
]

const TrendingContainer = () => {
    return (
        <div className="flex flex-col items-center w-full pr-6">
            <h1 className="font-bold mt-2 text-xl">
                Trending Posts
            </h1>
            {
                trendingPosts.map(post => {
                    return <TrendingCard trendingPostInfo={post}/>
                })
            }
            <span></span>
        </div>
    )
}

export default TrendingContainer;