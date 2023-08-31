import React from "react";
import "./about.css"
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Link from '@mui/material/Link';

export default class About extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            openOne: false,
            openTwo: false,
            openThree: false
        };

        this.handleLinkOne = this.handleLinkOne.bind(this);
        this.handleLinkTwo = this.handleLinkTwo.bind(this);
        this.handleLinkThree = this.handleLinkThree.bind(this);
    }

    handleLinkOne() {
        this.setState({openOne: !this.state.openOne});
    }
    handleLinkTwo() {
        this.setState({openTwo: !this.state.openTwo});
    }
    handleLinkThree() {
        this.setState({openThree: !this.state.openThree});
    }

    render() {
        return (
            <div className="body-about">
            <p className="pt-5">¿Quiénes somos?</p>
            <div className="info-about">
                <p>
                    OERinspanish es una plataforma de recursos educacionales 
                    en español gratuitos, única en su género al ofrecer materiales
                    creados tanto por estudiantes participantes en el programa de servicio 
                    comunitario <a className="inTextLink" onClick={this.handleLinkOne}> Feliz en la Comunidad </a> como 
                    por <a className="inTextLink" onClick={this.handleLinkTwo} >instructores de la Universidad de California del Sur (USC)</a>.<br/><br/>
                    
                    El proyecto, dirigido por las profesoras Mercedes Fages Agudo, Jamie Fudacz y 
                    Liana Stepanyan, nace con la esperanza de beneficiar a la comunidad más allá del 
                    entorno universitario y permitir el intercambio de conocimientos fuera de los límites tradicionales 
                    del aula de clase.<br/><br/>
                    
                    Los interesados podrán leer, descargar, copiar, imprimir o enlazar los textos 
                    completos de estas unidades citando la fuente o autor/es.
                </p>
            </div>

            <p className="mt-4">Futuras Colaboraciones</p>
            <div className="info-about">
                <p className="pb-5">Si tienen ideas, sugerencias o comentarios, por favor, contáctenos. <a href="mailto:oerin.spanish@usc.edu">oerin.spanish@usc.edu</a></p>
            </div>

            <Dialog
                open={this.state.openOne}
                onClose={this.handleLinkOne}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    Feliz en la Comunidad
                </DialogTitle>
                <div>
                    <p className="ps-4 pe-4" style={{marginBottom: 0}} id="alert-dialog-description">
                    Feliz en la Comunidad es un programa de alcance comunitario que, desde 2014, 
                    permite a los estudiantes de español de niveles intermedios y avanzados de la 
                    Universidad de California del Sur colaborar con escuelas del área metropolitana de Los Ángeles.<br/><br/>
                    A través de este programa de aprendizaje y servicio, los estudiantes practican español 
                    y sus habilidades de liderazgo mientras establecen conexiones con vecindarios cultural y 
                    socialmente diversos de Los Ángeles. Durante su participación, los estudiantes de USC 
                    crean materiales que posteriormente son compartidos con centros colaboradores
                    a través de nuestra plataforma o a través de visitas en persona a diversos centros colaboradores. <br/><br/>
                    Los interesados podrán leer, descargar, copiar, imprimir o enlazar los textos completos de estas 
                    unidades citando la fuente o autor/es.
                    </p>
                </div>
                    
                <DialogTitle id="alert-dialog-title">
                    Centros Colaboradores:
                </DialogTitle>
                <div className="ps-4 pe-4">
                    <Link target="_blank" href="https://dornsife.usc.edu/joint-educational-project">JOINT EDUCATIONAL PROJECT - USC</Link>
                    <br/><br/>

                    <Link target="_blank" href="https://calcreative.org/about/us/">CALCREATIVE</Link>
                    <br/><br/>

                    <Link target="_blank" href="https://www.ednovate.org/hybrid">USC HYBRID HIGH SCHOOL</Link>
                    <br/><br/>

                    <Link target="_blank" href="https://www.gusd.net/muir">JOHN MUIR ELEMENTARY SCHOOL (GLENDALE)</Link>
                    <br/><br/>

                    <Link target="_blank" href="https://www.larchmontcharter.org/lfp-campus">LARCHMONT CHARTER SCHOOL (LAFAYETTE PARK CAMPUS)</Link>
                    <br/><br/>

                    <Link target="_blank" href="https://www.educacionyfp.gob.es/eeuu/en/convocatorias-programas/convocatorias-eeuu/isa.html">INTERNATIONAL SPANISH ACADEMY (LOS ÁNGELES)</Link>
                    <br/><br/>
                
                </div>
                </Dialog>

                <Dialog
                    open={this.state.openThree}
                    onClose={this.handleLinkThree}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Centros colaboradores"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                        
                        </DialogContentText>
                    </DialogContent>
                </Dialog>

                <Dialog
                    open={this.state.openTwo}
                    onClose={this.handleLinkTwo}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Materiales creados por instructores de la Universidad de California del Sur"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                        Creado por el profesorado del programa básico de enseñanza del español  del Departamento de 
                        Culturas Latinoamericanas e Ibéricas de la Universidad del Sur de California, este repositorio 
                        hace accesibles materiales didácticos desde la óptica de la interacción entre la lengua española, 
                        la cultura hispana y la práctica docente. A partir de su vasta experiencia en el aula, el profesorado 
                        ha creado unidades que dan cobertura a una amplia variedad temática, centrándose en el desarrollo de 
                        diferentes destrezas de la lengua española. El objetivo es promover el pensamiento crítico y el intercambio 
                        de ideas y opiniones entre el alumnado, así como facilitar la comprensión de aspectos y convenciones 
                        socio-culturales del mundo hispano. Cada una de las unidades está etiquetada con metas educativas, estructuras 
                        gramaticales y el nivel. Los interesados podrán leer, descargar, copiar, imprimir o enlazar los textos completos 
                        de estas unidades citando la fuente o autor/es.
                        </DialogContentText>
                    </DialogContent>
                </Dialog>
            
        </div>
        );
    }
}
