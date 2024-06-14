import React from "react";
import { AiOutlineLike } from "react-icons/ai";

const TrendingCard = ({trendingPostInfo}) => {
    const {text, likes, username} = trendingPostInfo;
    
    return (
        <div className="p-2 border-black border-b-2 w-full text-sm mt-1 rounded-t-xl 
        hover:cursor-pointer hover:shadow-lg hover:shadow-gray-500 hover:scale-105 hover:bg-white/80 duration-500
        ">
            <h1>{text}</h1>
            <div className="text-xs flex items-center justify-around">
                <span>from: {username}</span>
                <div className="flex items-center">
                    <AiOutlineLike className=""/>
                    <span>{likes}</span>
                </div>
            </div>
        </div>
    )
}

export default TrendingCard;