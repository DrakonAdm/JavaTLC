package drakin.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import drakin.dao.CarDao;
import drakin.model.Car;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;
import org.json.JSONObject;


import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@WebServlet("/car")
public class CarController extends HttpServlet {

    @DependencyInjection
    private CarDao dao;

    public CarDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        Car car = dao.get(id).get();

        PrintWriter out = response.getWriter();
        String json = new JSONObject(car).toString();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            buffer.append(inputLine);
        }
        JSONObject jsonObject = new JSONObject(buffer.toString());
        Map<String, Object> map = jsonObject.toMap();
        Car car = new Car();
        car.setName((String)map.get("name"));
        car.setSpeed(Integer.parseInt((String)map.get("speed")));
        dao.put(car);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(car).toString());

    }
}
