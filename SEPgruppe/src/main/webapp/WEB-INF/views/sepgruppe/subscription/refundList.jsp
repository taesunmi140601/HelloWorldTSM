<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<section class="section-padding">
<div class="card mb-4">
    <div class="card-header">
        <i class="fas fa-table me-1"></i>
        환불 요청 관리
    </div>
    <div class="card-body">
        <!-- colgroup 적용을 위한 style -->
        <table id="datatablesSimple">
            <thead>
                <tr>
                    <th><i class="bi bi-check-square" style="color:green;"></i></th>
                    <th>No</th>
                    <th>아이디</th>
                    <th>고객사명</th>
                    <th>구독상품명</th>
                    <th>구독일자</th>
                    <th>결제일</th>
                    <th>마지막결제일</th>
                    <th>승인여부</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <div>
                            <input class="form-check-input" type="checkbox" id="checkboxNoLabel">
                        </div>
                    </td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>
                        <select class="form-select" aria-label="Default select example">
                            <option selected>승인</option>
                            <option value="1">거절</option>
                        </select>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="d-grid gap-2 col-1 mx-auto">
            <input type="button" class="btn btn-outline-success" value="저장">
        </div>
    </div>
</div>
</section>