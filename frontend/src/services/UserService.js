import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8080/home/";

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

export default {
    getUserRecords,
    getLearningRecords,
    updateUser
};