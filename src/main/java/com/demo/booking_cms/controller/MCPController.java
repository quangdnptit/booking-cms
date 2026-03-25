package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.response.SeatResponse;
import com.demo.booking_cms.service.SeatService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class MCPController {

    private final SeatService seatService;

    // Tool: getSeat
    @PostMapping("/tools/getSeat")
    public Map<String, Object> getSeat(@RequestBody Map<String, String> request) {
        String seatId = request.get("seatId");

        SeatResponse seat = seatService.findById(UUID.fromString(seatId));

        return Map.of(
                "status", "success",
                "data", seat
        );
    }

    @GetMapping("/tools")
    private Object listTools() {
        return Map.of(
                "tools", List.of(
                        Map.of(
                                "name", "getSeat",
                                "description", "Get order by ID",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "orderId", Map.of(
                                                        "type", "string"
                                                )
                                        ),
                                        "required", List.of("orderId")
                                )
                        )
                )
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JsonRpcRequest {
        public String jsonrpc;
        public String id;
        public String method;
        public Map<String, Object> params;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class JsonRpcResponse {
        public String jsonrpc = "2.0";
        public String id;
        public Object result;

        public JsonRpcResponse(String id, Object result) {
            this.id = id;
            this.result = result;
        }
    }

    @PostMapping
    public JsonRpcResponse handle(@RequestBody JsonRpcRequest request) {

        try {
            if ("tools/list".equals(request.method)) {
                return new JsonRpcResponse(request.id, listTools());
            }

            if ("tools/call".equals(request.method)) {
                return new JsonRpcResponse(request.id, callToolSafe(request.params));
            }

            if ("initialize".equals(request.method)) {
                return new JsonRpcResponse(request.id, initialize());
            }

            return error(request.id, "Unknown method");

        } catch (Exception e) {
            e.printStackTrace();
            return error(request.id, e.getMessage());
        }
    }

    private JsonRpcResponse error(String id, String message) {
        return new JsonRpcResponse(id, Map.of(
                "code", -32000,
                "message", message == null || message.isBlank() ? "Unknown error" : message
        ));
    }

    /**
     * Minimal MCP tools/call handler.
     * Expected params: { "name": "<toolName>", "arguments": { ... } }
     */
    private Object callToolSafe(Map<String, Object> params) {
        if (params == null) {
            throw new IllegalArgumentException("Missing params");
        }

        Object toolNameObj = params.get("name");
        String toolName = toolNameObj == null ? "" : toolNameObj.toString();
        Object argsObj = params.get("arguments");
        Map<?, ?> args = (argsObj instanceof Map<?, ?> m) ? m : Map.of();

        if ("getSeat".equals(toolName)) {
            Object seatIdObj = args.get("seatId");
            String seatId = seatIdObj == null ? "" : seatIdObj.toString().trim();
            if (seatId.isBlank()) {
                throw new IllegalArgumentException("seatId is required");
            }

            SeatResponse seat = seatService.findById(UUID.fromString(seatId));
            if (seat == null) {
                throw new IllegalArgumentException("Seat not found");
            }

            // MCP tool result shape (simple): { status, data }
            return Map.of("status", "success", "data", seat);
        }

        throw new IllegalArgumentException("Unknown tool: " + toolName);
    }

    private Object initialize() {
        return Map.of(
                "protocolVersion", "2024-11-05",
                "capabilities", Map.of(
                        "tools", Map.of()
                ),
                "serverInfo", Map.of(
                        "name", "my-java-mcp",
                        "version", "1.0.0"
                )
        );
    }
}