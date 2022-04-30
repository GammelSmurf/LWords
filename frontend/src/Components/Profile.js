import React, {useEffect, useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {PieChart, Pie, Cell, Legend, Tooltip, AreaChart, XAxis, YAxis, CartesianGrid, Area, ResponsiveContainer, BarChart, Bar} from "recharts";
import AuthService from '../services/AuthService';
import UserService from '../services/UserService';
import RecordService from "../services/RecordService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faBolt, faTrophy} from '@fortawesome/free-solid-svg-icons'

const Profile = () =>{
    const [validated, setValidated] = useState(false);
    const [isModalActive, setIsModalActive] = useState(false);
    const currentUser = AuthService.getCurrentUser();
    const [progressLength, setProgressLength] = useState(0);
    const [translationCount, setTranslationCount] = useState(0);
    const [pieData, setPieData] = useState([]);
    const [areaData, setAreaData] = useState([]);
    const [progressData, setProgressData] = useState([]);
    const [learnedWordsCount, setLearnedWordsCount] = useState(0);
    const [hardestWord, setHardestWord] = useState('');

    useEffect(() => {
        RecordService.getStatistic().then(
            (response) => {
                let dataPie = [];
                let dataArea = [];
                let dataProgress = [];

                if(!(response.data.completedRecCount === 0 && response.data.newRecCount === 0 && response.data.inProgressRecCount === 0)){
                    dataPie.push({name: 'Completed', value: response.data.completedRecCount});
                    dataPie.push({name: 'New', value: response.data.newRecCount});
                    dataPie.push({name: 'In progress', value: response.data.inProgressRecCount});
                }

                response.data.areaStatistic.forEach(item => {
                    dataArea.push({id: item.id, name: item.dayOfWeek, correctAnsCount: item.totalCorrectAnsCount, incorrectAnsCount: item.totalIncorrectAnsCount, learnedWordsCount: item.totalLearnedWordsCount})
                });

                let i = 0;
                response.data.progressStatistic.forEach(item=> {
                    dataProgress.push({count: item, progress: i});
                    i++;
                });

                setProgressData(dataProgress);
                setPieData(dataPie);
                dataArea.sort((a,b) => a.id > b.id ? 1 : -1);
                setAreaData(dataArea);
                setLearnedWordsCount(response.data.learnedWordsCount);
                setHardestWord(response.data.hardestWord);
            }
        );
    }, []);

    const pieColors = [{
        id: 0,
        color: '#91D0A9'
    }, {
        id: 1,
        color: '#AAAAAA'
    },{
        id: 2,
        color: '#E3716A'
    }]

    const handleModalClose = () => {setIsModalActive(false);};

    const onChangeProgressLength = (e) => {
        setProgressLength(e.target.value);
    };

    const onChangeTranslationCount = (e) => {
        setTranslationCount(e.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;
        if (form.checkValidity() === false)
            setValidated(true);
        else
            setIsModalActive(true);
    }

    const handleSave = () => {
        if (translationCount)
             currentUser.translationCount = translationCount;
         if (progressLength)
             currentUser.progressLength = progressLength;
         localStorage.setItem("user", JSON.stringify(currentUser));
         const user = {id: currentUser.id, translationCount: currentUser.translationCount, progressLength: currentUser.progressLength}
         UserService.updateUser(user)
             .then(() => {setIsModalActive(false); window.location.reload();});
    }

    const renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, index }) => {
        const RADIAN = Math.PI / 180;
        const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
        const x = cx + radius * Math.cos(-midAngle * RADIAN);
        const y = cy + radius * Math.sin(-midAngle * RADIAN);

        return (
            <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central" fontSize={24}>
                {pieData[index].value > 0 && pieData[index].value}
            </text>
        );
    };

    const isBlendStroke = () => {
        let flag;
        pieData.filter(item => item.value === 0).length === 2 ? flag = true : flag = false;
        return flag;
    }

    const BarCustomTooltip = ({ active, payload, label }) => {
        if (active && payload && payload.length) {
            return (
                <div className='barTooltip'>
                    <p>{`Progress: ${label}`}</p>
                    <p>{`Number of words: ${payload[0].value}`}</p>
                </div>
            );
        }
        return null;
    };

        return(
            <div className='container'>
                <div className='wrapper'>
                    <div>
                        <div className='standardPageHeader'>
                            <h2>Statistics</h2>
                            <hr />
                        </div>
                        <div className="row">
                            { pieData.length !== 0 &&
                            <div className='col-sm-4' style={{textAlign: 'center', marginTop: '20px'}}>
                                <h4>Table records</h4>
                                <ResponsiveContainer width={'100%'} height={260}>
                                    <PieChart>
                                        <Pie data={pieData} dataKey="value" nameKey="name" cx="50%" cy="50%"
                                             outerRadius={80} fill="#8884d8" paddingAngle={0} label={renderCustomizedLabel} labelLine={false} isAnimationActive={false} blendStroke={isBlendStroke()}>
                                            {
                                                pieData.map((entry, index) => (
                                                    <Cell key={`cell-${index}`} fill={pieColors[index].color}/>
                                                ))
                                            }
                                        </Pie>
                                        <Legend verticalAlign="bottom"/>
                                        <Tooltip />
                                    </PieChart>
                                </ResponsiveContainer>
                            </div>}
                            <div className="col-sm-8">
                                <div className="row">
                                    <div className="col-sm-6 text-center" style={{marginTop: '20px'}}>
                                        <h4>Learned words <FontAwesomeIcon icon={faTrophy} color='#91D0A9'/></h4>
                                        <p>{learnedWordsCount}</p>
                                    </div>
                                    <div className="col-sm-6 text-center" style={{marginTop: '20px'}}>
                                        <h4>The hardest word <FontAwesomeIcon icon={faBolt} color='#EB6864'/></h4>
                                        <p>{hardestWord}</p>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-12 text-center" style={{marginTop: '10px'}}>
                                        <h4>Progress</h4>
                                        <ResponsiveContainer width={'100%'} height={200}>
                                            <BarChart data={progressData}>
                                                <CartesianGrid strokeDasharray="3 3" />
                                                <XAxis dataKey="progress" />
                                                <YAxis />
                                                <Tooltip content={BarCustomTooltip}/>
                                                <Bar dataKey="count" fill="#E3716A" name='Number of words' barSize={30}/>
                                            </BarChart>
                                        </ResponsiveContainer>

                                    </div>
                                </div>
                            </div>

                        </div>

                        <div className="col-12 text-center" style={{marginTop: '20px'}}>
                            <h4>Activity</h4>
                            <ResponsiveContainer width={'100%'} height={250}>
                                <AreaChart data={areaData}>
                                    <defs>
                                        <linearGradient id="colorIncorrect" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="5%" stopColor="#EB6864" stopOpacity={0.8}/>
                                            <stop offset="95%" stopColor="#EB6864" stopOpacity={0}/>
                                        </linearGradient>
                                        <linearGradient id="colorCorrect" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                                            <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                                        </linearGradient>
                                        <linearGradient id="colorLearned" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="5%" stopColor="#336699" stopOpacity={0.8}/>
                                            <stop offset="95%" stopColor="#336699" stopOpacity={0}/>
                                        </linearGradient>
                                    </defs>
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <Tooltip />
                                    <Area type="monotone" dataKey="correctAnsCount" stroke="#82ca9d" fillOpacity={1} fill="url(#colorCorrect)" name='Correct answers' isAnimationActive={false}/>
                                    <Area type="monotone" dataKey="incorrectAnsCount" stroke="#EB6864" fillOpacity={1} fill="url(#colorIncorrect)" name='Wrong answers' isAnimationActive={false}/>
                                    <Area type="monotone" dataKey="learnedWordsCount" stroke="#336699" fillOpacity={1} fill="url(#colorLearned)" name='Learned words' isAnimationActive={false}/>
                                </AreaChart>
                            </ResponsiveContainer>
                        </div>

                    </div>
                    <div style={{marginTop: '50px', marginBottom: '30px'}}>
                        <div className='standardPageHeader'>
                            <h2>Settings</h2>
                            <hr />
                        </div>

                        <Form className="col-sm-4" noValidate validated={validated} onSubmit={handleSubmit}>
                            <Form.Group controlId="progressLength">
                                <Form.Label><b>Progress length</b></Form.Label>
                                <Form.Control
                                    type="number"
                                    onChange={onChangeProgressLength}
                                    placeholder={currentUser.progressLength}
                                    min="1"
                                    max="20"
                                />
                                <Form.Text className="text-danger">
                                    The words with progress greater than or equal to current value will be marked as completed!
                                </Form.Text>
                            </Form.Group>
                            <Form.Group controlId="translationCount">
                                <Form.Label><b>Max number of translations</b></Form.Label>
                                <Form.Control
                                    type="number"
                                    onChange={onChangeTranslationCount}
                                    placeholder={currentUser.translationCount}
                                    min="1"
                                    max="20"
                                />
                                <Form.Text className="text-danger">
                                    Relevant to new added words!
                                </Form.Text>
                            </Form.Group>
                            <Button className="formSubmitButton" type='submit'>Save</Button>
                        </Form>
                    </div>
                    <Modal show={isModalActive} onHide={handleModalClose}
                           animation={true}>
                        <Modal.Header>
                            <Modal.Title>Confirmation</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <Form.Text>
                                Do you really want to save current settings?
                            </Form.Text>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleModalClose}>
                                No
                            </Button>
                            <Button variant="primary" onClick={handleSave}>
                                Yes
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </div>
            </div>
        )
}
export default Profile;