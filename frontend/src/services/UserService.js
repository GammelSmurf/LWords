import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8090/core/";

const getUserRecords = () => {
    return axios.get(API_URL, { headers: authHeader() });
};

const getLearningRecords = () => {
    return axios.get(API_URL + "learning", { headers: authHeader() });
}

const updateUser = (user) => {
    return axios
        .put(API_URL + "profile", user, {headers: authHeader()})
}

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
    getUserRecords,
    getLearningRecords,
    updateUser
};