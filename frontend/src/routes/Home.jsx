import React from "react";
import PostsContainer from "../components/Post/PostsContainer";
import Navbar from "../components/Navbar/Navbar";
import PorfileCard from "../components/Profile/ProfileCard";
import TrendingContainer from "../components/Trending/TrendingContainer";

const Home = () => {
    return (
        <div className="bg-blue-200 h-screen overflow-scroll overflow-x-hidden">
            <Navbar className=""/>
            <div className="grid grid-cols-7 w-full relative pt-4">
                <PorfileCard/>
                <PostsContainer/>
                <TrendingContainer />
            </div>
        </div>
    )
}

export default Home;