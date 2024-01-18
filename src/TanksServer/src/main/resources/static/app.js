var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

let currentUUID = localStorage.getItem('UUID');

stompClient.connect({ UUID: currentUUID }, onConnected, onError);

function signInRegistrationUser() {
    const subscription = stompClient.subscribe(`/topic/registration.${localStorage.getItem('clientUuid')}`, (message) => {
        if (message.body == 'false'){
             $('#err-sign').text("You are already registered");
        } else {
             $('#err-sign').text("You are registered");
        }
        subscription.unsubscribe();
    });
    const name = $("#name").val();
    const password = $("#password").val();
    if (!name || !password) {
        $('#err-sign').text('Enter name and password');
    } else {
        stompClient.send('/app/registration', {}, JSON.stringify({'uuid': localStorage.getItem('clientUuid'), 'name': name, 'password': password}));
    }
}

function signUpUser() {
    const subscription = stompClient.subscribe(`/topic/error.${localStorage.getItem('clientUuid')}`, (message) => {
            if (message.body == 'Wrong'){
                 $('#err-sign').text("Wrong login or password");
            }
            subscription.unsubscribe();
    });

    const subscription2 = stompClient.subscribe(`/topic/signUp.${localStorage.getItem('clientUuid')}`, (message) => {
        const name = message.body;
        console.log("from server client name!_!_!: "+name);
        localStorage.setItem('MyName', name);
        stompClient.subscribe(`/topic/signUp.${localStorage.getItem('MyName')}`, (message) => {
                 const string = message.body;
                 $('#err-sign').text(string);
        });

        subscription2.unsubscribe();
    });

    const name = $("#name").val();
    const password = $("#password").val();
    stompClient.send('/app/signUp', {}, JSON.stringify({'uuid': localStorage.getItem('clientUuid'), 'name': name, 'password': password}));
}

function onConnected() {
    localStorage.setItem('clientUuid', uuidv4());
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function subscribeToPlayerId() {
    stompClient.subscribe(`/topic/joinRoom.player.${localStorage.getItem('MyName')}`, (message) => {
        console.log("you opponent: ", message.body);
        out = 0;
        var subscribeOp = stompClient.subscribe(`/topic/joinRoom.opponent.${message.body}`);
        localStorage.setItem('opName', message.body);
        stompClient.subscribe(`/topic/coordinates.${localStorage.getItem('MyName')}`, (coordinates) => {
            const coord = JSON.parse(coordinates.body);
            squareEnemy.x = coord.x;
            drawPlayers(ctx, canvas);
        });
        var subscribeBull = stompClient.subscribe(`/topic/bullets.${localStorage.getItem('MyName')}`, (bullets) => {
            const bullet = JSON.parse(bullets.body);
            console.log("bullet: ", bullet);
            var opBullet = { image: imageBullet2, x: bullet.x - 5, y: bullet.y - 300, width: 10, height: 20, speed: 10 };
            opBullets.push(opBullet);
            opBullets.forEach(function(element) {
                console.log(element);
            });
        });
        stompClient.subscribe(`/topic/outRoom.${localStorage.getItem('MyName')}`, (messageOut) => {
            console.log(messageOut.body);
            localStorage.removeItem('opName');
            myHealth = 3;
            opHealth = 3;
            if (messageOut.body == "l") {
                out = 1;
            } else {
                out = 2;
            }
            subscribeOp.unsubscribe();
            subscribeBull.unsubscribe();
        });

         stompClient.subscribe(`/topic/bang.${localStorage.getItem('MyName')}`, (msg) => {
            console.log(msg.body);

             if (msg.body == "o") {
                myHealth = myHealth - 1;
             } else {
                opHealth = opHealth - 1;
             }

             console.log("current HP: ", myHealth);
         });
    })
}

document.addEventListener('keydown', function(event) {
    var keyCode = event.keyCode;
    if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40) {
            bullets.forEach(function(element) {
              console.log(element);
            });

        square["userName"] = localStorage.getItem('MyName');
        stompClient.send(`/app/coordinates.${localStorage.getItem('opName')}`, {}, JSON.stringify(square));
    }
})

function sendCoordinates(message) {
    stompClient.send(`/app/coordinates.${message.body}`, {}, JSON.stringify(square));
}

function sendJoinRoomRequest() {
    subscribeToPlayerId();
    stompClient.send("/app/joinRoom", {}, JSON.stringify({ name: localStorage.getItem('MyName'), 'height': square.height ,'width': square.width}))
}

function sendCoordinatesBullets(message) {
    console.log('in sendCoordBullets: ' + message);
    message.userName = localStorage.getItem('MyName');
    stompClient.send(`/app/bullets.${localStorage.getItem('opName')}`, {}, JSON.stringify(message));
}

console.log(stompClient.subscriptions);

$(function () {
    $("#join").click(() => sendJoinRoomRequest());
    $("#singIn").click(() => signInRegistrationUser());
    $("#singUp").click(() => signUpUser());
});
