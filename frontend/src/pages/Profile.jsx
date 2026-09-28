import { useEffect, useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

function Profile() {
    const [profile, setProfile] = useState(null);
    const [bio, setBio] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {
        try {
            const response = await api.get(
                "/api/users/me/profile"
            );

            setProfile(response.data);
            setBio(response.data.bio || "");

        } catch (error) {
            console.error(
                "Profile error:",
                error.response?.data || error
            );
        } finally {
            setLoading(false);
        }
    };

    const updateProfile = async (e) => {
        e.preventDefault();

        try {
            const response = await api.put(
                "/api/users/me/profile",
                {
                    bio
                }
            );

            setProfile(response.data);

            alert("Profile updated successfully!");

        } catch (error) {
            console.error(
                "Update profile error:",
                error.response?.data || error
            );

            alert("Failed to update profile");
        }
    };

    if (loading) {
        return (
            <>
                <Navbar />

                <div className="profile-page">
                    <div className="profile-loading">
                        Loading profile...
                    </div>
                </div>
            </>
        );
    }

    if (!profile) {
        return (
            <>
                <Navbar />

                <div className="profile-page">
                    <div className="profile-error">
                        Profile not found.
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />

            <main className="profile-page">

                <div className="profile-card">

                    {/* Profile header */}

                    <div className="profile-header">

                        <div className="profile-avatar">
                            {profile.username
                                ?.charAt(0)
                                .toUpperCase()}
                        </div>

                        <h1>
                            @{profile.username}
                        </h1>

                        <p className="profile-bio">
                            {profile.bio ||
                                "No bio yet."}
                        </p>

                    </div>

                    {/* Statistics */}

                    <div className="profile-stats">

                        <div className="profile-stat">
                            <strong>
                                {profile.followersCount}
                            </strong>

                            <span>
                                Followers
                            </span>
                        </div>

                        <div className="profile-stat">
                            <strong>
                                {profile.followingCount}
                            </strong>

                            <span>
                                Following
                            </span>
                        </div>

                    </div>

                    <div className="profile-divider"></div>

                    {/* Edit profile */}

                    <div className="edit-profile">

                        <h2>Edit Profile</h2>

                        <form onSubmit={updateProfile}>

                            <label>
                                Bio
                            </label>

                            <textarea
                                value={bio}
                                onChange={(e) =>
                                    setBio(e.target.value)
                                }
                                placeholder="Tell people about yourself..."
                                rows="4"
                                maxLength="160"
                            />

                            <div className="bio-counter">
                                {bio.length}/160
                            </div>

                            <button
                                type="submit"
                                className="profile-button"
                            >
                                Save Changes
                            </button>

                        </form>

                    </div>

                </div>

            </main>
        </>
    );
}

export default Profile;