const searchPaging = (_this) => {
    pageNum = _this.getAttribute("page-num");

    if (pageNum != undefined && pageNum != null) {
        document.getElementById("pageNum").value = pageNum;
    }
    submitForm();
}


const searchList = () => {

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
    if (pageNum != undefined && pageNum != null) {
        document.getElementById("pageNum").value = pageNum;
    }
    document.getElementById("boardForm").submit();
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
        })
        .catch(function (error) {
            console.log(error);
            alert("게시글 삭제 실패");
        })
        .then(function () {
            console.log("then");
            gotoList();
        });
}

function insertBoard(mode) {
    const formData = new FormData(document.getElementById("boardForm"));
    const id = formData.get("id");

    if (mode == 'insert') {

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