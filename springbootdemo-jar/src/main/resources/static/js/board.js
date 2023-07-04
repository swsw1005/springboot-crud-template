const searchPaging = (_this) => {
    pageNum = _this.getAttribute("page-num");

    if (pageNum != undefined && pageNum != null) {
        document.getElementById("pageNum").value = pageNum;
    }
    submitForm();
}


const searchList = () => {

    const validateMsg = validate();
    if (validateMsg.length > 0) {
        alert(validateMsg + ".");
        return;
    }

    const formData = new FormData(document.getElementById("boardForm"));
    console.log(document.getElementById("startDate").valueAsDate);
    console.log(document.getElementById("endDate").valueAsDate);
    if (document.getElementById("endDate").valueAsDate != null && document.getElementById("startDate").valueAsDate != null) {
        if (document.getElementById("endDate").valueAsDate < document.getElementById("startDate").valueAsDate) {
            alert("시작일이 종료일보다 클 수 없습니다.");
            return;
        }
    }

    console.log(formData);

    axios.post('/board/list', formData)
        .then(function (response) {
            console.log(response);
            document.getElementById("listLocation").innerHTML = response.data;
        })
        .catch(function (error) {
            console.log(error);
            document.getElementById("listLocation").innerHTML = '<h6>조회에 실패했습니다.</h6>'
        })
        .then(function () {
            console.log("then");
        });

}

const submitForm = (pageNum) => {

    const validateMsg = validate();
    if (validateMsg.length > 0) {
        alert(validateMsg);
        return;
    }

    if (pageNum != undefined && pageNum != null) {
        document.getElementById("pageNum").value = pageNum;
    }
    document.getElementById("boardForm").submit();
}

const validate = () => {

    const searchKeyword = document.getElementById("searchKeyword");
    const searchType = document.getElementById("searchType");

    if (searchType.value != null && searchType.value.length > 0) {
        if (searchKeyword.value.length > 1) {
            //
        } else {
            return ("검색어는 2글자 이상 입력해주세요");
        }
    } else {
        if (searchKeyword.value.length > 0) {
            return "검색 조건을 선택해주세요.";
        } else {
            //
        }
    }
    return "";
}

const gotoRegister = () => {
    location.href = "/board/register"
}

const gotoView = (_this) => {
    const id = _this.getAttribute("data-id");
    location.href = "/board/" + id;
}


const gotoList = () => {
    location.href = '/board';
}

const gotoViewById = (id) => {
    location.href = '/board/' + id;
}


function resize() {
    var wd = window.innerWidth - 40;
    var target = document.getElementById("textArea");

    if (target != undefined && target != null) {
        target.style.width = wd + 'px';
        target.style.width = '100%';
    }
}

function deleteById(_this) {
    var id = _this.getAttribute("data-id");

    if (id != undefined && id != null) {
        console.log("id", id);
    }

    if (!confirm("삭제하시겠습니꺄?")) {
        return;
    }

    axios.delete('/api/v1/board/' + id)
        .then(function (response) {
            console.log(response);
            alert("게시글 삭제 성공");
            gotoList();
        })
        .catch(function (error) {
            console.log(error);
            alert("게시글 삭제 실패");
        })
        .then(function () {
            console.log("then");
        });
}

function insertBoard(mode) {
    const formData = new FormData(document.getElementById("boardForm"));
    const id = formData.get("id");

    if (mode == 'insert') {

        const validateMsg = validateInsert(formData);
        if (validateMsg.length > 0) {
            alert(validateMsg + ".");
            return;
        }

        axios.put('/api/v1/board/', formData)
            .then(function (response) {
                console.log(response);
                console.log(response.data);
                // alert("게시글 등록 성공");
                gotoViewById(response.data.id);
            })
            .catch(function (error) {
                console.log(error);
                alert("게시글 등록 실패");
                gotoList();
            })
            .then(function () {
                console.log("then");
            });
    } else {

        formData.append("writer", "writer");

        const validateMsg = validateInsert(formData);
        if (validateMsg.length > 0) {
            alert(validateMsg + ".");
            return;
        }

        axios.post('/api/v1/board/', formData)
            .then(function (response) {
                console.log(response);
                alert("게시글 수정 성공")
            })
            .catch(function (error) {
                console.log(error);
                alert("게시글 수정 실패")
            })
            .then(function () {
                console.log("then");
                gotoViewById(id);
            });
    }

}

const validateInsert = (_formData) => {

    const validateRule = {
        "title": {
            "length": 2,
            "msg": "제목을 2글자 이상 입력해주세요."
        },
        "body": {
            "length": 2,
            "msg": "내용을 2글자 이상 입력해주세요."
        },
        "writer": {
            "length": 2,
            "msg": "작성자를 2글자 이상 입력해주세요."
        },
    }

    for (let keysetElement of Object.keys(validateRule)) {

        var length = validateRule[keysetElement]['length'];
        var msg = validateRule[keysetElement]['msg'];

        console.log(keysetElement, "___", _formData.get(keysetElement));

        if (_formData.get(keysetElement) != null && _formData.get(keysetElement) != undefined) {
            console.log(_formData.get(keysetElement).length)
            if (_formData.get(keysetElement).length >= length) {
                continue;
            }
        }
        return msg;
    }
    return "";

}