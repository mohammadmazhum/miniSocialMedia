import { Link, useNavigate } from "react-router-dom";

function Navbar() {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        navigate("/login");
    };

    return (
        <nav className="navbar">

            <Link to="/" className="logo">
                ☁️ CloudSocial
            </Link>

            <div className="nav-links">

                <Link to="/">
                    Home
                </Link>

                <Link to="/profile">
                    Profile
                </Link>

                <button onClick={logout}>
                    Logout
                </button>

            </div>

        </nav>
    );
}

export default Navbar;