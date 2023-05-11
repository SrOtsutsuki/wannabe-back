<html>
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&display=swap" rel="stylesheet">
    <style>

        .body {
            font-family: 'Montserrat', sans-serif;
        }

        td {
            text-align: center;
        }

        .m0 {
            margin: 0px;
        }

        .mb15 {
            margin-bottom: 15px;
        }

        .small {
            font-size: 13px;
        }

        .mb50 {
            margin-bottom: 50px;
        }
    </style>
</head>
<body class="body">
<table class="table-wrapper" style="width: 100%;text-align: center;">
    <tr>
        <td>
            <table class="default" style="width: 25%; border: 1px solid black; padding: 15px; margin: 0 auto;"
                   bgcolor="white">
                <tr>
                    <td>
                        <h2>
                            ¡Ya tienes tu/s entrada/s!
                        </h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        <hr class="mb50" size="1px" color="black"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img width="500" class="mb50"
                             src="cid:${BANNER_IMG}" alt="imagenCartel">
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>
                            <img width="30" src="https://cdn-icons-png.flaticon.com/512/6777/6777443.png"
                                 alt="iconoBola">
                            <span>${TICKET_NAME}</span>
                        </p>
                        <p>
                            <img width="30" src="https://cdn-icons-png.flaticon.com/512/8403/8403154.png"
                                 alt="iconoCalendar">
                            <span>${FORMAT_DATE}</span>
                        </p>
                        <p class="mb50">
                            <img width="30" src="https://cdn-icons-png.flaticon.com/512/8738/8738883.png"
                                 alt="iconoLocation">
                            <span>${BUSINESS_NAME}</span>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <hr size="1px" color="black"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h1>Este es tu código QR</h1>
                        <p>
                            <img style="width: 250px;" src="cid:${QR}" alt="QrCode">
                        </p>
                        <p class="mb15">
                            <span>ID:&nbsp;</span>
                            <span>${RESERVE_CODE}</span>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="m0">${PERSON_NAME}</p>
                        <p class="m0 small">Contacto:&nbsp;${PERSONA_MAIL}</p>
                        <p class="mb15 m0 small"> ${PERSON_PHONE}&nbsp;//&nbsp;${PERSON_DOCUMENT} </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>Si has realizado una reserva de mas de una entrada, te adjuntaremos en este correo todos los
                            códigos QR.</p>
                        <p>Recuerda que cada código Qr es <b>único e intransferible.</b> Una vez utilizado, se marcara
                            en puerta como utilizado y no hay vuelta atrás.</p>
                        <p class="mb15">Asegurate de que cada uno tiene el suyo y no utilizar dos códigos QR para una
                            misma persona.</p>
                        <p class="m0">NO SE ADMITEN DEVOLUCIONES</p>
                        <p class="m0 mb15">SE RESERVA EL DERECHO DE ADMISIÓN</p>

                    </td>
                </tr>
                <tr>
                    <td>
                        <hr size="1px" color="black"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>DETALLES DE TU COMPRA</p>
                        <p class="m0 small"><b>TOTAL POR PERSONA // ${TICKET_PRICE}€ //</b></p>
                        <h2 class="m0 mb50"><b>TOTAL // ${TOTAL_PURCHASE}€ //</b></h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="mb15">DETALLES DEL PROMOTOR:</p>
                    </td>
                </tr>
                <tr>
                    <td style="text-align:left ;">
                        <p class="m0">WANNABE MODERNOS S.L</p>
                        <p class="m0">Plaza Zorrilla nº1-3º</p>
                        <p class="m0">49023 - Zamora - España</p>
                        <p class="m0 mb50">C.I.F: B02790491</p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h1>TE ESPERAMOS PARA DISFRUTAR JUNTOS</h1>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>