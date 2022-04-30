import React, {useEffect, useRef, useState} from "react";
import {Card, Button, Modal, ProgressBar} from 'react-bootstrap';
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, {Navigation} from 'swiper/core';
import 'swiper/swiper-bundle.css';
import UserService from "../services/UserService";
import BootstrapTable from "react-bootstrap-table-next";
import AuthService from "../services/AuthService";
import RecordService from "../services/RecordService";
import {faCheck, faTimes, faTrophy} from '@fortawesome/free-solid-svg-icons'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const Learning = (props) => {
    const [content, setContent] = useState([]);
    const [tableData, setTableData] = useState([]);
    const [currentRecord, setCurrentRecord] = useState();
    const [inputTranslation, setInputTranslation] = useState('');
    const [isInputDisabled, setIsInputDisabled] = useState(false);
    const [isCardShown, setIsCardShown] = useState(false);
    const [isModalActive, setIsModalActive] = useState(false);
    const [isLearningFinished, setIsLearningFinished] = useState(false);
    const [isInfoButtonVisible, setIsInfoButtonVisible] = useState(false);
    const [isAddButtonVisible, setIsAddButtonVisible] = useState(false);
    const [isProgressNotZero, setIsProgressNotZero] = useState(false);
    const [isAnswerGiven, setIsAnswerGiven] = useState(false);
    const [currentUser, setCurrentUser] = useState();
    const [correctAns, setCorrectAns] = useState(0);
    const [incorrectAns, setIncorrectAns] = useState(0);
    const [learnedWords, setLearnedWords] = useState(0);
    const [slider, setSlider] = useState();
    const textInput = useRef(null);
    const changeSlideInput = useRef(null);
    const card = useRef(null);
    const progressLength = Number(AuthService.getCurrentUser().progressLength);


    useEffect(() => {
        UserService.getLearningRecords().then(
            (response) => {
                if(response.data.length !== 0){
                    setContent(response.data);
                    setCurrentRecord(response.data[0]);
                    setCurrentUser(AuthService.getCurrentUser());
                }
                else
                    setIsModalActive(true);
            },
            (error) => {
                const _content =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setContent(_content);
            }
        )
    }, []);

    const progressFormatter = (cell) => {
        let labelContent = '';
        cell === progressLength ? labelContent = 'completed' : labelContent = `${cell}` + '/' + `${progressLength}`;
        return (
            <span>
                <ProgressBar variant='success' style={{marginTop : '4px'}} now={cell / progressLength * 100} label={labelContent}/>
            </span>
        );
    }

    const columns = [{
        dataField: 'phrase',
        text: 'Word / phrase',
    }, {
        dataField: 'progress',
        text: 'Progress',
        formatter: progressFormatter
    }];

    const handleReverso = () => {
        window.open('https://context.reverso.net/' +
            '%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/' +
            '%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9' +
            '-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9/' + currentRecord.phrase);
    }

    const onChangeInputTranslation = (e) => {
        setInputTranslation(e.target.value.toLowerCase());

    }

    const handleSubmit = (e) => {
        switch(e.keyCode) {
            case 37:
                slider.slidePrev();
                break;
            case 39:
                slider.slideNext();
                break;
            case 13:
                if(slider.slides.length === 1)
                    setIsLearningFinished(true);
                setIsInputDisabled(true);
                setIsAnswerGiven(true);

                const translations = currentRecord.translations.split(';').slice(0,-1)
                let flag = false;
                translations.forEach(el => {
                    if(el === inputTranslation)
                        flag = true
                })
                const record = {};
                if(flag){
                    textInput.current.style.backgroundColor = 'rgba(34,178,76,0.7)';
                    setIsInfoButtonVisible(true);
                    setCorrectAns(correctAns + 1);
                    currentRecord.correctAnsCount += 1;
                    currentRecord.progress += 1;
                    if(currentRecord.progress === Number(currentUser.progressLength)){
                        setLearnedWords(learnedWords + 1)
                        currentRecord.finished = true;
                    }
                    record.result = true
                }
                else {
                    setIsCardShown(true);
                    textInput.current.style.backgroundColor = 'rgba(245,122,0,0.7)';
                    if(inputTranslation !== '')
                        setIsAddButtonVisible(true);
                    setIncorrectAns(incorrectAns + 1);
                    currentRecord.incorrectAnsCount += 1;
                    if(currentRecord.progress > 0)
                    {
                        setIsProgressNotZero(true);
                        currentRecord.progress -= 1;
                    }
                    record.result = false
                }
                RecordService.updateRecord(currentRecord.id,currentRecord).then();

                record.id = currentRecord.id;
                record.phrase = currentRecord.phrase;
                record.progress = currentRecord.progress;
                tableData.push(record);
                setTableData(tableData);
                changeSlideInput.current.focus();
        }
    }

    const handleOnSlideChange = () => {
        console.log(slider)
        setCurrentRecord(content.find(rec => rec.phrase === slider.slides[slider.activeIndex].childNodes[0].nodeValue));
        setIsCardShown(false);
        setIsInfoButtonVisible(false);
        setIsAddButtonVisible(false);
        setIsInputDisabled(false);
        setInputTranslation('');
        textInput.current.value = '';
        textInput.current.style.backgroundColor = 'white';
        textInput.current.focus();
        if(isAnswerGiven){
            setIsAnswerGiven(false);
            slider.removeSlide(slider.previousIndex);
            slider.update();
        }
    }

    const rowClasses = (row) => {
        let style;
        row['result'] ? style = 'correctRow'
        : style = 'incorrectRow'
        return style;
    }

    const handleModalClose = () => {
        props.history.push('/home')
    }

    const handleAddTrans = () => {
        const record = {};
        textInput.current.style.backgroundColor = 'rgba(34,178,76,0.7)';
        setIsAddButtonVisible(false);
        setIncorrectAns(incorrectAns - 1);
        setCorrectAns(correctAns + 1);
        currentRecord.incorrectAnsCount -= 1;
        currentRecord.correctAnsCount += 1;
        isProgressNotZero ? currentRecord.progress += 2 : currentRecord.progress += 1;
        setIsProgressNotZero(false);
        if(currentRecord.progress === Number(currentUser.progressLength)){
            setLearnedWords(learnedWords + 1)
            currentRecord.finished = true;
        }
        currentRecord.translations += inputTranslation + ';';
        RecordService.updateRecord(currentRecord.id,currentRecord).then();

        record.result = true;
        record.id = currentRecord.id;
        record.phrase = currentRecord.phrase;
        record.progress = currentRecord.progress;
        tableData.pop();
        tableData.push(record);
        setTableData(tableData);
        changeSlideInput.current.focus();
    }

    const handleChangeSlide = (e) =>{
        if(e.keyCode === 37)
            slider.slidePrev();
        if(e.keyCode === 39)
            slider.slideNext();
    }

    SwiperCore.use([Navigation]);
    return (
        <div className='container'>
            <div style={{paddingTop: '50px'}}>
                <div className='row'>
                    <div className='cards col-sm-6'>
                        <Swiper onSwiper={(swiper)=>setSlider(swiper)} navigation={true} style={{marginBottom: '40px'}} onSlideChangeTransitionEnd={handleOnSlideChange}>
                            {content.map(record =>
                                <SwiperSlide key={record.id} >
                                    {record.phrase}
                                </SwiperSlide>
                            )}
                        </Swiper>
                        {isCardShown &&
                        <Card ref={card} className={'bg-primary text-white translationCardOpen'}>
                            <Card.Body>
                                <Card.Title>Translations</Card.Title>
                                <Card.Text>
                                    {currentRecord && currentRecord.translations.split(';').join(', ').slice(0,-2)}
                                </Card.Text>
                                <Card.Footer>
                                    <div style={{margin: '0'}}>
                                        <Card.Text>
                                            Correct answers: {currentRecord && currentRecord.correctAnsCount}
                                        </Card.Text>
                                        <Card.Text>
                                            Wrong answers: {currentRecord && currentRecord.incorrectAnsCount}
                                        </Card.Text>
                                    </div>
                                    <Button className='learningCardButton'
                                            onClick={() => handleReverso()}>Examples</Button>
                                </Card.Footer>
                            </Card.Body>
                        </Card> }


                        <hr/>
                        <div className="learningInputGroup">
                            <div style={{'display':'inline-block'}}>
                                <p className="text-muted" style={{margin: 0}}>Type translation</p>
                                <input ref={textInput} className="userTranslationInput form-control" type="text"
                                       onChange={onChangeInputTranslation}
                                       onKeyUp={(e) => handleSubmit(e)}
                                       disabled={isInputDisabled} autoFocus/>
                            </div>
                            {isAddButtonVisible &&
                                <Button variant='primary'
                                        onClick={handleAddTrans}>
                                    Add
                                </Button>}
                            {isInfoButtonVisible &&
                                <Button variant='primary'
                                        onClick={()=>{setIsCardShown(!isCardShown);changeSlideInput.current.focus();}}>
                                    Info
                                </Button>}
                        </div>
                        <div style={{width:0,overflow:'hidden'}} >
                            <input ref={changeSlideInput}  onKeyUp={e => handleChangeSlide(e)} />
                        </div>

                    </div>
                    <div className="learningResults col-sm-6">
                        <BootstrapTable bordered={false} keyField='id' data={tableData} columns={columns} rowClasses={ rowClasses }
                                        noDataIndication={() => <p>Type translation to see result</p>}/>
                    </div>
                    <Modal show={isModalActive} onHide={handleModalClose}
                           animation={true}>
                        <Modal.Header>
                            <Modal.Title>Notification</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            There are no words to learn! Please add new records.
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="primary" onClick={handleModalClose}>
                                Ok
                            </Button>
                        </Modal.Footer>
                    </Modal>
                    {isLearningFinished && <div className="finish">
                        <h5>You have finished practise</h5>
                        <hr/>
                        <div className="results">
                            <div><FontAwesomeIcon icon={faCheck} color='#EB6864'/> Correct: {correctAns}</div>
                            <div><FontAwesomeIcon icon={faTimes} color='#EB6864'/> Incorrect: {incorrectAns}</div>
                            <div><FontAwesomeIcon icon={faTrophy} color='#EB6864'/> Words learned: {learnedWords}</div>
                        </div>
                        <div className="finishButtons">
                            <Button variant="primary" onClick={() => window.location.reload()}>
                                Try again
                            </Button>
                            <Button variant="secondary" onClick={() => props.history.push('/home')}>
                                Go to home
                            </Button>
                        </div>
                    </div>}
                </div>
            </div>
        </div>
    );
}

export default Learning;