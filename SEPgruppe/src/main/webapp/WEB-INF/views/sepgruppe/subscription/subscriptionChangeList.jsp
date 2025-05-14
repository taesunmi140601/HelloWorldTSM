<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<section class="section-padding">
<div class="card mb-4">
    <div class="card-header">
        <i class="fas fa-table me-1"></i>
        구독변경관리
    </div>
    <div class="card-body">
        <table id="datatablesSimple">
            <thead>
                <tr>
                    <th>No</th>
                    <th>고객사명</th>
                    <th>구독등급</th>
                    <th>구독인원</th>
                </tr>
            </thead>
            <tbody>
                <!-- tr태그 for문으로 데이터가 있을경우 생기도록 해야 함 -->
                <tr>
                    <td>데이터</td>
                    <td>데이터</td>
                    <td>
                        <select class="form-select" aria-label="Default select example">
                            <option selected>구독등급</option>
                            <option value="1">수정해야됨1</option>
                            <option value="2">수정해야됨2</option>
                        </select>
                    </td>
                    <td>
                        <select class="form-select" aria-label="Default select example">
                            <option selected>구독인원</option>
                            <option value="1">수정해야됨1</option>
                            <option value="2">수정해야됨2</option>
                        </select>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="d-grid gap-2 d-md-block d-md-flex justify-content-center" id="button">
            <input type="button" class="button" value="승인">
            <input type="button" class="buttonDanger" value="거절">
        </div>
    </div>
</div>


</section>