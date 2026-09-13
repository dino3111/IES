package pt.ua.claudino.backend;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/api/items")
public class ItemController {

    private List<Map<String, Object>> items = new ArrayList<>();
    private int idCounter = 1;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return items;
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> item) {
        item.put("id", idCounter++);
        items.add(item);
        return item;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        items.removeIf(item -> (int)item.get("id") == id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable int id, @RequestBody Map<String, Object> updated) {
        for (Map<String, Object> item : items) {
            if ((int)item.get("id") == id) {
                item.put("name", updated.get("name"));
                return item;
            }
        }
        return null;
    }

    @PostMapping("/import")
    public Map<String, Object> importCSV(@RequestParam("file") MultipartFile file) {

        Map<String, Object> result = new HashMap<>();
        result.put("imported", 10);
        result.put("errors", 2);

        return result;
    }
}