# Apache-Skywalking-8.3.0
Thông tin tóm tắt về CVE-2020-9483

•    CVE ID: CVE-2020-9483

•    Mức độ: High

•    Ảnh hưởng: Apache SkyWalking từ bản 6.x đến 8.3.0 (cụ thể khi dùng H2 database)

•    Mô tả: Lỗ hổng SQL Injection tồn tại trong lớp H2QueryDAO, cho phép kẻ tấn công thực thi câu lệnh SQL tùy ý thông qua truy vấn từ phía người dùng được chèn trực tiếp vào truy vấn SQL mà không được escape hoặc parameterize.

Cụ thể hơn trong SkyWalking

•    API /graphql cho phép người dùng gửi truy vấn có chứa metricName

•    OAP nhận truy vấn này, truyền thẳng metricName vào queryLogs(...)

•    metricName được dùng trực tiếp trong sql.append(...) hoặc SelectQueryImpl.from(...)

•    Khi sử dụng Database là H2, attacker có thể chèn payload SQL vào CSDL được Skywalking sử dụng trong cấu hình mặc định là h2 và được khởi động với quyền sa.
![image](https://github.com/user-attachments/assets/2f091608-c5ab-480f-8b6e-77e019d6ff93)
![image](https://github.com/user-attachments/assets/3490aff6-bb36-49b3-86e1-1703cd36998f)

Cơ sở dữ liệu h2 có chức năng ghi tệp FILE_WRITE (yêu cầu quyền quản trị để thực thi) và cả nội dung tệp và tên tệp đều có thể được kiểm soát.

![image](https://github.com/user-attachments/assets/ca477221-9f30-409a-a057-3d860106d5a3)
![image](https://github.com/user-attachments/assets/344c31ed-9d6b-426c-9d95-da5fad5a10ce)

Một hàm LINK_SCHEMA khác có thể chỉ định và khởi tạo yêu cầu jdbc hoặc jndi, nhưng vì không có sự phụ thuộc vào tomcat và springboot trong môi trường đích nên không thể sử dụng jndi chung 
trong phiên bản jdk cao hơn và cần phải tìm chuỗi tham chiếu trong sự phụ thuộc cục bộ.

![image](https://github.com/user-attachments/assets/1bdb9db1-2d5e-48d7-b744-a7796f096ffe)
![image](https://github.com/user-attachments/assets/8a4785d2-7f78-4194-aaa7-2f8a94eae342)
