import React from "react";

const ProfileCard = () => {

    return (
        <div className="py-2 px-4 border border-1 border-gray-700 flex flex-col p-2 rounded-lg max-h-[360px]
        w-full shadow-md col-start-1 col-end-3 items-center ml-8
        hover:cursor-pointer hover:shadow-gray-500 duration-500 hover:shadow-2xl hover:border-black hover:bg-white/50">
        <div className="">
            <img src="https://pbs.twimg.com/profile_images/1797718457393319936/jD90K3os_400x400.jpg" alt="profile_picture" 
            className="w-[150px] rounded-full m-auto"/>
            <h1 className="font-bold text-lg mt-2 ml-2"> Username </h1>
            <p className="mt-2 overflow-hidden text-sm text-gray-700"> Lorem ipsum dolor sit, amet consectetur adipisicing elit. Consequuntur vitae, quas 
                voluptatem deleniti ipsa doloremque ducimus maxime molestiae qui nam illum repellat neque eos 
                quis aperiam adipisci. Officiis, accusantium sapiente. </p>
            <div className="flex">
            <span className="m-auto hover:text-blue-700">Posts: 5</span>
            <span className="m-auto hover:text-blue-700">Likes: 4</span>
            </div>
        </div>
        </div>
    )
}

export default ProfileCard;