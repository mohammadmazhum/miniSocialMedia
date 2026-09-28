import { useState } from "react";
import api from "../services/api";

function CreatePost({ onPostCreated }) {
    const [content, setContent] = useState("");
    const [image, setImage] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!content.trim() && !image) {
            alert("Write something or select an image");
            return;
        }

        try {
            setLoading(true);

            let imageUrl = "";

            // Step 1: Upload image to Cloudinary
            if (image) {
                const formData = new FormData();
                formData.append("file", image);

                const uploadResponse = await api.post(
                    "/api/images/upload",
                    formData
                );

                imageUrl = uploadResponse.data.url;
            }

            // Step 2: Create post
            const postResponse = await api.post("/api/posts", {
                content: content,
                imageUrl: imageUrl
            });

            // Clear form
            setContent("");
            setImage(null);

            // Tell Home page about the new post
            if (onPostCreated) {
                onPostCreated(postResponse.data);
            }

            alert("Post created successfully!");

        } catch (error) {
            console.error(
                "Create post error:",
                error.response?.data || error
            );

            alert("Failed to create post");

        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="create-post">
            <h2>Create Post</h2>
            <textarea
                className="post-input"
                placeholder="What's on your mind?"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                rows="4"
            />

            <form onSubmit={handleSubmit}>

                <br />
                <br />

                <input
                    type="file"
                    accept="image/*"
                    onChange={(e) => setImage(e.target.files[0])}
                />

                <br />
                <br />

                <button className="post-button" type="submit" disabled={loading}>
                    {loading ? "Posting..." : "Post"}
                </button>

            </form>
        </div>
    );
}

export default CreatePost;