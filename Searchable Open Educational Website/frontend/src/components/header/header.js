import React from "react";
import "./header.css"
import USClogo from './USClogo.png';

export default class Header extends React.Component {
    render() {
        return (
            <div class="container-fluid headerrr" >
                <div class = "row head">
                    <div class="col oone">
                        <p class = "deptTitle">Departamento de Culturas Latinoamericanas e Ibéricas</p>
                        <p class= "deptBody">Programa básico de lengua española</p>
                         <p class="deptBody2">oerin.spanish@usc.edu<br />
                        </p>
                    </div>
                    <div class="col twoo">
                    <img src={USClogo} alt="logo"height={150}/>
                    </div>
                </div>
            </div>


        );}
}
