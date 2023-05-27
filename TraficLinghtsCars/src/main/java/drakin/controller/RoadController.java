package drakin.controller;

import drakin.model.Car;
import drakin.model.Lane;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import drakin.dao.RoadDao;
import drakin.model.Road;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@WebServlet("/road")
public class RoadController extends HttpServlet {
    @DependencyInjection
    private RoadDao dao;

    public RoadDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Long id = Long.parseLong(request.getParameter("id"));
        Road road = dao.get(id).get();

        PrintWriter out = response.getWriter();
        String json = new JSONObject(road).toString();
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

        List<Lane> lanes = new ArrayList<>();
        for (Object id: (List<Object>) map.get("lanes")) {
            Lane lane = new Lane();
            lane.setIdentity(Long.parseLong((String) id));
            lanes.add(lane);
        }

        Road road = new Road();
        road.setLanes(lanes);

        dao.put(road);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(road).toString());

    }
}
