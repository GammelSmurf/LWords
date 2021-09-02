import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8080/auth/";

const login = (userName, password) => {
    return axios
        .post(API_URL + "signin", {
            userName,
            password
        })
        .then(response => {
            if (response.data.token) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }
            return response.data;
        });
};

const register = (userName, password) => {
    return axios
        .post(API_URL + "signup", {
            userName,
            password
        }, {headers: authHeader()})
};

const logout = () => {
    localStorage.removeItem("user");
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
};

const getUsers = () => {
    return axios.get(API_URL + "users", { headers: authHeader() })
}

const removeUser = (userId) => {
    return axios
        .delete(API_URL + "users/" + userId, {headers: authHeader()})
        .then(() => {
            window.location.reload();
        });
}


export default {
    login,
    register,
    logout,
    getCurrentUser,
    getUsers,
    removeUser
};