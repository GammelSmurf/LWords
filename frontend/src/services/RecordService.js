import axios from "axios";
import authHeader from "./AuthHeader";


const API_URL = "http://localhost:8090/core/";
let responseMessage;

    const addRecord = (newPhrase) =>{
        return axios
            .post(API_URL, {
                newPhrase
            }, {headers: authHeader()})
            .then(response => {
                responseMessage = response.data;
            });
    };

    const getResponse = () => {
        return responseMessage;
    }

    const removeRecords = (recordIds) =>{
    return axios
        .post(API_URL + 'delete/',{recordIds}, {headers: authHeader()})
        .then(() => {
            window.location.reload();
        })
    };

    const updateRecord = (recordId, record) => {
        return axios
            .put(API_URL + "learning/" + recordId, record, {headers: authHeader()})
    }

    const importCSV = (formData) => {
        return axios
            .post(API_URL + "upload/", formData,{headers: authHeader()}).then(
                response => responseMessage = response.data.successCount
            )
    }

    const getStatistic = () => {
        return axios.get(API_URL + "profile", { headers: authHeader() });
    }

export default {addRecord, removeRecord: removeRecords, getMessage: getResponse, updateRecord, importCSV, getStatistic};