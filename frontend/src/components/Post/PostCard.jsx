import React from "react";
import { AiOutlineLike } from "react-icons/ai";
import { FaCommentAlt } from "react-icons/fa";

const PostCard = ({postData}) => {
    const {text, likes, username, comments, liked} = postData;

    return (
        <div className="py-2 px-4 border border-1 border-gray-700 w-[360px] flex flex-col md:w-[720px] mt-2 rounded-tr-md 
        hover:scale-105 duration-200 hover:border-black hover:shadow-xl shadow-md">
            <div className="flex items-center">
                <img src="https://pbs.twimg.com/profile_images/1797718457393319936/jD90K3os_400x400.jpg" alt="profile_picture" className="w-[40px] rounded-full"/>
                <h1 className="font-bold text-lg mt-2 ml-2"> {username} </h1>
            </div>
            <p className="mt-2"> {text} </p>
            <div className="text-sm text-gray-600 flex w-1/6 justify-around mt-2">
            <div className={`flex hover:text-black hover:cursor-pointer ${liked ? 'text-blue-500 hover:text-blue-900' : 'hover:text-black'}`}>
            <AiOutlineLike className="m-auto mx-1" />
             {likes}
            </div>
                <div className= "flex hover:text-black hover:cursor-pointer"> <FaCommentAlt className="m-auto mx-1"/> {comments} </div>
            </div>
        </div>
    )
}

export default PostCard;