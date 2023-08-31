import React from "react";
import "./footer.css";
import DornsifeLogo from "./DornsifeLogo.png";
import CC from "./cc.png";
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

export default class Footer extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false
        };

        this.handleCopyright = this.handleCopyright.bind(this);
    }

    handleCopyright() {
        this.setState({open: !this.state.open});
    }

    render() {
        return (
            <div className="container-fluid">
                <div className="row footer">
                    <div className="col onee">
                        <img src={DornsifeLogo} alt="logo" />
                    </div>
                    <div className="col twwo">
                        <img src={CC} alt="cc" onClick={this.handleCopyright}/>
                    </div>
                </div>

                <Dialog
                    open={this.state.open}
                    onClose={this.handleCopyright}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">
                        {"Creative Commons- Licencia no comercial 4.0 internacional (CC BY-NC 4.0)"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            Todos los materiales se pueden descargar, de acuerdo con la siguiente licencia: Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)<br/><br/>
                            Los usuarios podrán:<br/>
                            · <strong>Compartir</strong> — copiar y distribuir los materiales en cualquier formato.<br/>
                            · <strong>Adaptar</strong> — combinar, cambiar y agregar.<br/><br/>
                            Bajo las siguientes condiciones:<br/>
                            · <strong>Atribució</strong>n — Los usuarios deberán mencionar la fuente, incluir el enlace de la licencia e indicar los cambios hechos.<br/>
                            · <strong>Uso</strong> no comercial —Los materiales no se deberán usar para fines comerciales.<br/>
                            · <strong>Sin</strong> restricciones — Los permisos se extienden a los terceros. No se deberán aplicar medios legales o técnicos que restrinjan el uso de los materiales por terceros.<br/><br/>
                            Más sobre la licencia: Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)<br/>
                        </DialogContentText>
                    </DialogContent>
                </Dialog>
            </div>
        );
    }
}
