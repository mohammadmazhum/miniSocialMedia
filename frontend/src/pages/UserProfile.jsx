import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";

function UserProfile() {
    const { username } = useParams();

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadProfile();
    }, [username]);

    const loadProfile = async () => {
        try {
            setLoading(true);
            setError("");

            console.log("Loading profile:", username);

            const response = await api.get(
                `/api/users/${username}`
            );

            console.log("Profile response:", response.data);

            setProfile(response.data);

        } catch (error) {
            console.error(
                "Profile error:",
                error.response?.data || error
            );

            setError(
                error.response?.data ||
                "Failed to load profile"
            );

        } finally {
            setLoading(false);
        }
    };

    const handleFollow = async () => {
        try {
            const response = await api.post(
                `/api/users/${username}/follow`
            );

            setProfile(response.data);

        } catch (error) {
            console.error(
                "Follow error:",
                error.response?.data || error
            );
        }
    };

    if (loading) {
        return <p>Loading profile...</p>;
    }

    if (error) {
        return (
            <div>
                <h2>Unable to load profile</h2>
                <p>{error}</p>
            </div>
        );
    }

    if (!profile) {
        return <p>User not found.</p>;
    }

    return (
        <div className="container">

            <div className="profile-card">

                <div className="avatar profile-avatar">
                    {profile.username
                        ?.charAt(0)
                        .toUpperCase()}
                </div>

                <h1>
                    @{profile.username}
                </h1>

                <p>
                    {profile.bio || "No bio yet."}
                </p>

                <div className="profile-stats">

                    <span>
                        <strong>
                            {profile.followersCount}
                        </strong>
                        {" "}Followers
                    </span>

                    <span>
                        <strong>
                            {profile.followingCount}
                        </strong>
                        {" "}Following
                    </span>

                </div>

                <button onClick={handleFollow}>
                    Follow / Unfollow
                </button>

            </div>

        </div>
    );
}

export default UserProfile;