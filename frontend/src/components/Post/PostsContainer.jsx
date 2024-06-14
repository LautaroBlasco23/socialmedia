import React, { useState } from "react";
import PostCard from "./PostCard";
import WriteNewPost from "./WriteNewPost";

const PostsContainer = () => {
    const [posts, setPosts] = useState([
        {
            "text": "Este vendría a ser mi primer posteo en la plataforma, es realmente emocionante",
            "likes": 12,
            "username": "Lautaro",
            "comments": 4,
            "liked": true,
        },
        {
            "text": "Segundo",
            "likes": 0,
            "username": "Lautaro",
            "comments": 4,
            "liked": true,
        },
        {
            "text": "Tercer",
            "likes": 120,
            "username": "Lautaro",
            "comments": 2,
            "liked": false,
        },
        {
            "text": "Kuarto Post",
            "likes": 3,
            "username": "Lautaro",
            "comments": 0,
            "liked": false,
        }
    ]);

    return (
        <div className="w-full flex flex-col items-center col-start-3 col-end-7 col-span-4">
            <WriteNewPost />
            {
                posts.map((post, index) => {
                    return <PostCard key={index} postData={post} />
                })
            }
        </div>
    )
}

export default PostsContainer;