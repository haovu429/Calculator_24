package hcmute.edu.vn.calculator_24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("sign")

    Button number0, number1, number2, number3, number4, number5, number6, number7, number8, number9, btnDot, btnClear, btnDiv, btnPlus, btnEqual, btnSub, btnMul;

    EditText editText;

    double current_value, operand2;// hai Toán hạng trong một phép tính
    int calculation = -1; // Phép tính thể hiện qua 0-cộng, 1-trừ, 2-nhân, 3-chia, -1(chưa chọn phép tính)

    String result; // Kết quả hiển thị kiểu string
    Boolean result_ok = true; // Cờ báo có kết quả, có thể hiển thị

    int negative = 1; // 1 là dương , khi nhập dấu âm sẽ mang  giá trị -1 và có thể lấy giá trị này nhân vào các số hạng để đổi dấu
    int last_click_type = -1;// Biến thể hiện kiểu nút chọn trước đó là loại nào(0-loại số; 1- loại phép tính; 2-dấu bằng ;3- âm)

    // Biến này cho biết kết quả vừa tính có đang hiển thị trên mang hình hay không
    // Trường hợp sau khi một phép tính được hoàn thành bằng dấu phép tính (+,-,x,/)
    // và nếu muốn nhập dấu chấm để thực hiện một phép tính mới thì sẽ xoá kết quả và hiển thị "0."
    boolean showing_results = false;

    boolean decimal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number0 = findViewById(R.id.number0); //Tìm lại button
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        btnClear = findViewById(R.id.btnClear);
        btnDot = findViewById(R.id.btnDot);
        btnEqual = findViewById(R.id.btnEqual);
        btnMul = findViewById(R.id.btnMultiply);
        btnPlus = findViewById(R.id.btnPlus);
        btnDiv = findViewById(R.id.btnDivide);
        btnSub = findViewById(R.id.btnSubtract);

        editText = findViewById(R.id.edt_Output);


        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_click(0);
            }
        });

        number1.setOnClickListener(view -> number_click(1));

        number2.setOnClickListener(view -> number_click(2));

        number3.setOnClickListener(view -> number_click(3));

        number4.setOnClickListener(view -> number_click(4));

        number5.setOnClickListener(view -> number_click(5));

        number6.setOnClickListener(view -> number_click(6));

        number7.setOnClickListener(view -> number_click(7));

        number8.setOnClickListener(view -> number_click(8));

        number9.setOnClickListener(view -> number_click(9));

        btnPlus.setOnClickListener(view -> {
            operation_click('+', "Cộng");
            calculation = 0; // 0: phép cộng
        });

        btnSub.setOnClickListener(view -> {
            if (last_click_type == 1 || last_click_type == -1) {
                negative_click("-");
                showing_results = false; // Nếu đã show kết quả rồi thì dặt lại cờ này đề nhập dấu chấm đối với số hạng thứ 2
            } else {
                System.out.println("last_click_type: " + last_click_type);
                System.out.println("Da vao ham tru");
                if (last_click_type != 3) {
                    operation_click('-', "Trừ");
                    calculation = 1; // 1: phép trừ
                }
            }

        });

        btnMul.setOnClickListener(view -> {
            operation_click('x', "Nhân");
            calculation = 2; // 2: phép nhân
        });

        btnDiv.setOnClickListener(view -> {
            operation_click('/', "Chia");
            calculation = 3; // 3: phép chia
        });

        btnClear.setOnClickListener(view -> {
            //Toast.makeText(MainActivity.this,"Bạn đã click",Toast.LENGTH_SHORT).show();
            //editText.setText(editText.getText().toString()+"0");
            clear_cache();
            //other_type_click();
        });

        btnDot.setOnClickListener(view -> {

            change_text_number_type(true);

            //Phân biệt đang trong quá trình thực hiện nhập số hạng thứ nhất hay số hạng thứ 2
            if (calculation == -1) {

                //Nếu bắt đầu một phép tính mới bằng dấu chấm thì tự động thêm số 0 và dấu chấm

                if (editText.getText().toString().equals("")) {
                    System.out.println("equals ");
                    clear_cache();
                    editText.append("0.");
                    current_value = 0;
                } else {
                    System.out.println("not equals ");

                    //Kiểm tra xem màn hình có đang show kết quả (do nhấn vào dấu bằng) hay không?
                    //Nếu có thì xoá kết quả trên màn hình và bắt đầu nhập số hạng thứ 2 với "0."
                    //Nếu không thì rơi vào trường hợp đang nhập dở số hạng thứ 2 và trước đó là các chữ số (chưa có dấu chấm-thập phân)
                    if (showing_results) {
                        editText.setText("0.");
                        current_value = 0;
                    } else {
                        if (!decimal) // Nếu trước đó chưa nhập số thập phân thì sẽ cho nhập dấu chấm
                        {
                            editText.append(".");
                        }
                    }
                }

            } else {
                //Nếu bắt đầu nhập số hạng thứ 2 bằng dấu chấm thì tự động thêm số 0 và dấu chấm

                if (editText.getText().toString().equals("")) {
                    // Trường hợp này không xảy ra vì trước khi nhập số hạng thứ 2 thì 1 là sẽ
                    // hiển thị dấu phép tính hai là sẽ hiển thị kết quả 3 là đang nhập dở số hạng thứ 2
                    System.out.println("equals ");
                    //clear_cache();
                    editText.append("0.");
                    operand2 = 0;
                } else {
                    System.out.println("not equals ");

                    //Kiểm tra xem màn hình có đang show kết quả (do nhấn vào dấu phép tính) hay không?
                    //Nếu có thì xoá kết quả trên màn hình và bắt đầu nhập số hạng thứ 2 với "0."
                    //Nếu không thì rơi vào trường hợp đang nhập dở số hạng thứ 2 và trước đó là các chữ số (chưa có dấu chấm-thập phân)
                    if (showing_results) {
                        editText.setText("0.");
                        operand2 = 0;
                    } else {
                        if (last_click_type == 1) {
                            editText.setText("0.");
                            operand2 = 0;
                        } else {
                            if (!decimal) // Nếu trước đó chưa nhập số thập phân thì sẽ cho nhập dấu chấm
                            {
                                editText.append(".");
                            }
                        }
                    }
                }
                //editText.append(".");
            }
            editText.setSelection(editText.getText().length());

            decimal = true; // đánh dấu trên editText đang có số thập phân, nếu nhập tiếp dấu chấm sẽ không nhận
            last_click_type = 0;// Vì khi nào click vào 0 thì cũng tồn tại giá trị nên xem nó như nhập một số
        });

        btnEqual.setOnClickListener(view -> {
            ///Chỉ khi xác định được phép tính thì mới bắt đầu tính
            if (calculation != -1) {
                calculate();
            }
            if(showing_results && last_click_type == 2)
                showing_results = false; // Nếu đã show kết quả rồi thì dặt lại cờ này đề nhập dấu chấm đối với số hạng thứ 2
            last_click_type = 2;
        });
    }

    private void number_click(int num) {
        change_text_number_type(negative != 1);

        //Nếu đang hiện kết quả (do nhấn dấu bằng) thì xoá kết quả và bắt đầu phép tính mới
        if (showing_results) {
            showing_results = false; // Nếu đã show kết quả rồi thì dặt lại cờ này đề nhập dấu chấm đối với số hạng thứ 2
        }

        //Nếu trước đó nhập phép tính thì xoá phép tình trên màn hình và bắt đầu nhập toán hạng thứ 2
        if (last_click_type == 1)
            editText.setText("");


        editText.append(String.valueOf(num));
        editText.setSelection(editText.getText().length());

        try {
            if (calculation == -1)
                current_value = Double.parseDouble(editText.getText().toString());
            else
                operand2 = Double.parseDouble(editText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Dữ liệu nhập không hợp lệ", Toast.LENGTH_SHORT).show();
            clear_cache();
            e.printStackTrace();
        }


        last_click_type = 0;
        showing_results = false;
    }

    private void negative_click(String sign) {
        change_text_number_type(true);
        negative = -1;

        //Nếu trước đó nhập phép tính thì xoá phép tình trên màn hình và bắt đầu nhập toán hạng thứ 2
        if (last_click_type == 1)
            editText.setText("");

        editText.append(String.valueOf(sign));
        editText.setSelection(editText.getText().length());

        if (calculation == -1)
            current_value = 0;
        else
            operand2 = 0;

        last_click_type = 3;
    }


    private void operation_click(char ch, String opr_string) {
        // Không chọn các phép tính khi chưa có số hạng thứ nhất được khởi tạo từ người dùng
        if (last_click_type != -1) {
            Toast.makeText(MainActivity.this, opr_string, Toast.LENGTH_SHORT).show();

            //editText.setText(editText.getText().toString()+"0");
            editText.setText(String.valueOf(ch));

            if(showing_results)
                showing_results = false; // Nếu đã show kết quả rồi thì dặt lại cờ này đề nhập dấu chấm đối với số hạng thứ 2

            //Khi đã nhập toán hạng thứ nhất, chọn phép tính, nhập toán tử thứ 2 xong mà nhấn thêm một phép tính chứ không nhấn dấu = sẽ tự tính ngầm,
            if (last_click_type == 0 && calculation != -1) {
                calculate();
            }
            editText.setSelection(editText.getText().length());
            last_click_type = 1;
            negative = 1;
            // Khi nhập phép tính nghĩa là số thứ nhất đã nhập xong và bắt đầu nhập số thứ 2 vậy nên có thể cho nhập dấu châm
            decimal = false;
        }
    }

    private void clear_cache() {
        editText.setText("");
        editText.setSelection(editText.getText().length());
        current_value = 0;
        calculation = -1;
        result = "";
        last_click_type = -1;
        negative = 1;
        decimal = false;
        showing_results = false;
    }

    private void calculate() {
        //Tinh toan, tra ket qua
        result();

        if (result_ok) {
            //System.out.println("result = " + result);
            editText.setText(result);
            showing_results = true;
        } else {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            clear_cache();
        }

        editText.setSelection(editText.getText().length());

        negative = 1;
        calculation = -1; // Đánh dấu đã thực hiện xong phép tính và có thể thực hiện một phép tính mới bằng cách nhập số
        decimal = false;
    }

    private void result() {
        System.out.println("cal: " + calculation);
        result_ok = true;
        switch (calculation) {
            case 0:
                current_value += operand2;
                break;
            case 1:
                current_value -= operand2;
                break;
            case 2:
                current_value *= operand2;
                break;
            case 3:
                if (operand2 != 0)
                    current_value /= operand2;
                else {
                    result_ok = false;
                    result = "Lỗi chia cho 0";// có thể ghi tường minh lỗi ở đây mặc dù sẽ không hiển thị lên edit text
                    // nhưng khi báo lỗi sẽ xuất hiện, có hoặc ko cũng được
                }
                break;
            default:
                current_value = 0;
        }
        //System.out.println("lam tron = " + String.valueOf(Math.round(current_value)));

        try {
            // Nếu kết quả hợp lệ thì tinh chỉnh và in kết quả, nếu không, biến result sẽ thể hiện thông tin lỗi
            if (result_ok) {
                // Nếu như kết quả là số nguyên thì hiển thị số nguyên
                if (current_value == Math.round(current_value)) {
                    result = String.valueOf((int) Math.round(current_value));
                    //System.out.println("result2 = " + result);

                } else {
                    //Nếu kết quả là số thập phân thì hiển thị số thập phân
                    current_value = decimalFormat(current_value, 4); // Làm tròn tới 4 số sau dấu phẩy
                    result = String.valueOf(current_value);
                }
            }
        } catch (Exception e) {
            //Bắt lỗi, chủ yếu lỗi chia hết cho 0
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            clear_cache();//reset máy tính, về nguyên tình trạng ban đầu sau khi báo lỗi
        }
    }

    // Hàm làm tròn về số thập phân có zero_num chữ số sau số 0.
    private double decimalFormat(double decimal_num, int zero_num) {

        double scale = Math.pow(10, zero_num);

        return Math.round(decimal_num * scale) / scale;
    }

    // mode = true -> text; mode = false -> number
    private void change_text_number_type(boolean mode) {
        String temp = editText.getText().toString();

        if (mode) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        try {
            editText.setText(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}