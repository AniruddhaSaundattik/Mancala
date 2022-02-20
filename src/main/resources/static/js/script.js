$(document).ready(function () {
    let PLAYER1;
    let PLAYER2;
    const ENABLED_CLASS = 'enabled';
    const DISABLED_CLASS = 'disabled';
    let CURRENT_PLAYER = bConfig.player2;
    let jwtToken;

    generateJwt();
    loadGame();


    $('#show-hide-rules').click(function () {
        $('#rules').toggle();
    });

    $("#start-game").click(function () {
        $('#start-game').hide();
        loadGame(true);
    });

    function generateJwt() {
        $.ajax({
            url: '/secure/authenticate',
            type: 'POST',
            async: false,
            data:
                JSON.stringify({
                    userName: 'mancala',
                    password: 'mancala'
                }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (result) {
                jwtToken = result.jwt;
            },
            error: function (result) {
                alert('Status: ' + JSON.stringify(result));
            }
        });
    }

    function generatePits(number = 0, bigPit = '') {
        for (i = 0; i < number; i++) {
            var div = document.createElement('div');
            div.className = 'pit disabled';
            div.id = i;
            div.addEventListener('click', function (event) {
                event.preventDefault();
                pitClicked(this);
            })
            var currentDiv = document.getElementById(bigPit);
            currentDiv.before(div);
        }
        ;
    }

    function loadGame(newGame = false) {
        $('#game-over').hide();
        $('#turninfo').show();
        $.ajax({
            url: '/start',
            type: 'POST',
            data:
                JSON.stringify({
                    smallPits: bConfig.smallPits,
                    smallPitStonesCount: bConfig.smallPitStonesCount,
                    player1: bConfig.player1,
                    player2: bConfig.player2
                }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwtToken
            },
            success: function (result) {
                if (!newGame) {
                    generatePits(bConfig.smallPits, 'treasury1');
                    generatePits(bConfig.smallPits, 'treasury2');
                } else {
                    CURRENT_PLAYER = bConfig.player2;
                    generateJwt();
                }
                drawBoard(result);
            },
            error: function (result) {
                alert('Status: ' + JSON.stringify(result));
            }
        });
    }

    function drawBoard(result) {
        PLAYER1 = result.player1;
        PLAYER2 = result.player2;
        $.each(PLAYER1.smallPitsCount, function (i, item) {
            $('#player1 .pit').eq(i).html(item);
            if (CURRENT_PLAYER == PLAYER1.name && item > 0) {
                $('#player1 .pit').eq(i).removeClass(DISABLED_CLASS).addClass(ENABLED_CLASS);
            } else {
                $('#player1 .pit').eq(i).removeClass(ENABLED_CLASS).addClass(DISABLED_CLASS);
            }
        });

        $.each(PLAYER2.smallPitsCount, function (i, item) {
            $('#player2 .pit').eq(i).html(item);
            if (CURRENT_PLAYER == PLAYER2.name && item > 0) {
                $('#player2 .pit').eq(i).removeClass(DISABLED_CLASS).addClass(ENABLED_CLASS);
            } else {
                $('#player2 .pit').eq(i).removeClass(ENABLED_CLASS).addClass(DISABLED_CLASS);
            }
        });
        $('#player1 #treasury1 > label').html(PLAYER1.name);
        $('#player2 #treasury2 > label').html(PLAYER2.name);
        $('#player1 #treasury1 > span').html(PLAYER1.bigPitCount);
        $('#player2 #treasury2 > span').html(PLAYER2.bigPitCount);


        switch (CURRENT_PLAYER) {
            case PLAYER1.name:
                // $('#player1 .pit').removeClass(DISABLED_CLASS).addClass(ENABLED_CLASS);
                // $('#player2 .pit').removeClass(ENABLED_CLASS).addClass(DISABLED_CLASS);
                $('#turninfo > span').html(PLAYER1.name);
                break;
            case PLAYER2.name:
                // $('#player1 .pit').removeClass(ENABLED_CLASS).addClass(DISABLED_CLASS);
                // $('#player2 .pit').removeClass(DISABLED_CLASS).addClass(ENABLED_CLASS);
                $('#turninfo > span').html(PLAYER2.name);
                break;
        }
        if (result.gameOver) {
            $('#player1 .pit, #player2 .pit').removeClass(ENABLED_CLASS).addClass(DISABLED_CLASS);
            $('#game-over').show();
            $('#turninfo').hide();
            $('#winner').children('span').html(result.winner);
            $('#score').children('span').html(PLAYER1.score + '|' + PLAYER2.score);
            $('#start-game').show();
        }
    }

    function pitClicked(obj) {
        if ($(obj).hasClass(DISABLED_CLASS)) return;

        $.ajax({
            url: '/turn',
            type: 'POST',
            data:
                JSON.stringify({
                    player: CURRENT_PLAYER,
                    pitClicked: obj.id,
                    mancalaObj: {
                        player1: PLAYER1,
                        player2: PLAYER2
                    }
                }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwtToken
            },
            success: function (result) {
                let opponent = (CURRENT_PLAYER === PLAYER1.name) ? PLAYER2.name : PLAYER1.name
                CURRENT_PLAYER = result.repeatTurn ? CURRENT_PLAYER : opponent
                drawBoard(result);
            },
            error: function (result) {
                alert('Status: ' + JSON.stringify(result));
            }
        });
    }
});

