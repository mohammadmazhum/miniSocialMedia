import { useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";

function PostCard({ post, onPostUpdated }) {
    const [comment, setComment] = useState("");
    const [loading, setLoading] = useState(false);

    // Controls whether comments are visible
    const [showComments, setShowComments] = useState(false);

    const handleLike = async () => {
        try {
            await api.post(
                `/api/posts/${post.id}/like`
            );

            onPostUpdated();

        } catch (error) {
            console.error(
                "Like error:",
                error.response?.data || error
            );
        }
    };

    const handleComment = async (e) => {
        e.preventDefault();

        if (!comment.trim()) {
            return;
        }

        try {
            setLoading(true);

            await api.post(
                `/api/posts/${post.id}/comments`,
                {
                    text: comment
                }
            );

            setComment("");

            onPostUpdated();

        } catch (error) {
            console.error(
                "Comment error:",
                error.response?.data || error
            );
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        const confirmed = window.confirm(
            "Are you sure you want to delete this post?"
        );

        if (!confirmed) {
            return;
        }

        try {
            await api.delete(
                `/api/posts/${post.id}`
            );

            onPostUpdated();

        } catch (error) {
            console.error(
                "Delete error:",
                error.response?.data || error
            );

            alert("Failed to delete post");
        }
    };

    return (
        <div className="post-card">

            {/* =========================
                POST HEADER
            ========================= */}

            <div className="post-header">

                <div className="post-avatar">
                    {post.authorUsername
                        ?.charAt(0)
                        .toUpperCase()}
                </div>

                <div className="post-author-info">

                    <Link
                        to={`/user/${post.authorUsername}`}
                    >
                        @{post.authorUsername}
                    </Link>

                </div>

            </div>


            {/* =========================
                POST CONTENT
            ========================= */}

            <p className="post-content">
                {post.content}
            </p>


            {/* =========================
                POST IMAGE
            ========================= */}

            {post.imageUrl && (
                <img
                    className="post-image"
                    src={post.imageUrl}
                    alt="Post"
                />
            )}


            {/* =========================
                POST ACTIONS
            ========================= */}

            <div className="post-actions">

                {/* Like */}

                <button
                    className="like-button"
                    onClick={handleLike}
                >
                    ❤️ Like ({post.likes?.length || 0})
                </button>


                {/* Comments toggle */}

                <button
                    className="comment-toggle"
                    onClick={() =>
                        setShowComments(!showComments)
                    }
                >
                    💬 Comments ({post.comments?.length || 0})
                </button>


                {/* Delete */}

                {post.authorUsername ===
                    localStorage.getItem("username") && (

                    <button
                        className="delete-button"
                        onClick={handleDelete}
                    >
                        🗑️ Delete
                    </button>

                )}

            </div>


            {/* =========================
                COMMENT SECTION
            ========================= */}

            {showComments && (

                <div className="comment-section">

                    <h4>
                        Comments
                    </h4>


                    {/* Existing comments */}

                    <div className="comments">

                        {(!post.comments ||
                            post.comments.length === 0) ? (

                            <p className="no-comments">
                                No comments yet.
                            </p>

                        ) : (

                            post.comments.map(
                                (item, index) => (

                                    <div
                                        className="comment"
                                        key={index}
                                    >

                                        {/* Comment avatar */}

                                        <div className="comment-avatar">

                                            {(
                                                item.authorUsername ||
                                                item.authorEmail
                                            )
                                                ?.charAt(0)
                                                .toUpperCase()}

                                        </div>


                                        {/* Comment content */}

                                        <div className="comment-content">

                                            <strong>

                                                @
                                                {item.authorUsername ||
                                                    item.authorEmail?.split("@")[0]}

                                            </strong>

                                            <p>
                                                {item.text}
                                            </p>

                                        </div>

                                    </div>

                                )
                            )

                        )}

                    </div>


                    {/* =========================
                        ADD COMMENT
                    ========================= */}

                    <form
                        className="comment-form"
                        onSubmit={handleComment}
                    >

                        <input
                            className="comment-input"
                            type="text"
                            placeholder="Write a comment..."
                            value={comment}
                            onChange={(e) =>
                                setComment(e.target.value)
                            }
                        />

                        <button
                            type="submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Adding..."
                                : "Comment"}
                        </button>

                    </form>

                </div>

            )}

        </div>
    );
}

export default PostCard;