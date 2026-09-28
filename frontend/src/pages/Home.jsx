import { useEffect, useState } from "react";
import api from "../services/api";
import CreatePost from "../components/CreatePost";
import PostCard from "../components/PostCard";
import Navbar from "../components/Navbar";

function Home() {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        loadPosts();
    }, []);

    const loadPosts = async () => {
        try {
            const response = await api.get("/api/posts");
            setPosts(response.data);
        } catch (error) {
            console.error(
                "Failed to load posts:",
                error
            );
        }
    };

    const handlePostCreated = (newPost) => {
        setPosts((currentPosts) => [
            newPost,
            ...currentPosts
        ]);
    };

    const handlePostUpdated = () => {
        loadPosts();
    };

    const logout = () => {
        localStorage.removeItem("token");
        window.location.href = "/login";
    };

    return (
        <div>

            <Navbar />

            <main className="container">

                <CreatePost
                    onPostCreated={handlePostCreated}
                />

                <hr />

                <h2>Posts</h2>

                {posts.length === 0 ? (
                    <p>No posts yet.</p>
                ) : (
                    posts.map((post) => (
                        <PostCard
                            key={post.id}
                            post={post}
                            onPostUpdated={handlePostUpdated}
                        />
                    ))
                )}

            </main>

            

        </div>
    );
}

export default Home;