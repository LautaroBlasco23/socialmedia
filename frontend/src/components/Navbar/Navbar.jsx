import React, { useState, useEffect } from 'react';

const Navbar = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const userLoggedIn = localStorage.getItem('userToken');
    
        setIsLoggedIn(!!userLoggedIn); // !! convierte el valor a un booleano
    }, []);

    return (
        <div className="w-full flex bg-blue-950/60 text-black text-lg p-2 sticky top-0 z-50
        hover:bg-blue-950/90 duration-1000 ease-out hover:text-blue-300">
            <div className="m-auto hover:cursor-pointer hover:text-white hover:scale-110 transition-transform duration-200 ease-in-out">Home</div>
                {isLoggedIn ? (
                    <div className="flex w-1/5">
                        <div className="m-auto hover:cursor-pointer hover:text-white hover:scale-110 transition-transform duration-200 ease-in-out">Profile</div>
                    </div>
                ) : (
                    <div className="flex w-1/5">
                        <div className="m-auto hover:cursor-pointer hover:text-white hover:scale-110 transition-transform duration-200 ease-in-out">login</div>
                        <div className="m-auto hover:cursor-pointer hover:text-white hover:scale-110 transition-transform duration-200 ease-in-out">register</div>
                    </div>

                )}
        </div>
    )
}

export default Navbar;