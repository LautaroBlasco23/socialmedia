import React, { useState } from "react";

const WriteNewPost = () => {
    const [postInput, setPostInput] = useState("");

    const createPost = (e) => {
        console.log(e)
        alert();
        setPostInput("");
    }

    return (
        <div className="py-2 px-4 border border-1 border-gray-700 w-[360px] flex flex-col md:w-[720px] mt-2 rounded-tr-md 
        hover:scale-105 duration-200 hover:border-black hover:shadow-xl shadow-md">
           <div className="flex items-center">
                <img src="https://pbs.twimg.com/profile_images/1797718457393319936/jD90K3os_400x400.jpg" alt="profile_picture" className="w-[40px] rounded-full"/>
                <h1 className="font-bold text-lg mt-2 ml-2">Lautaro</h1>
            </div>
                <h2 className="font-bold text-lg mt-2 ml-2">Crea tu nuevo post:</h2>
            <input className="w-full h-1/2 p-2 mt-2" type="text" value={postInput} 
            onChange={(e) => setPostInput(e.target.value)} 
            placeholder="Escribe tu siguiente post aqui..."/>
            <div className="w-full flex justify-end my-2">
                <button className= "bg-white border border-black p-2 w-full max-w-[150px]
                hover:cursor-pointer hover:bg-black hover:text-white hover:border-white duration-700 ease-out"
                onClick={createPost}>Crear</button>
            </div>
        </div>
    )
}

export default WriteNewPost;