import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8090/auth/";

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

export default {
    login,
    register,
    logout,
    getCurrentUser,
    getUsers,
    removeUser
};