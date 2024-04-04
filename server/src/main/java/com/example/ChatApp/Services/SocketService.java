/*
 * package com.example.ChatApp.Services;
 * 
 * import java.util.ArrayList; import java.util.HashMap; import java.util.List;
 * import java.util.Map;
 * 
 * public class SocketService { public static Map<String, List<String>>
 * groupMembers = new HashMap<>();
 * 
 * public static void addUserToGroup(String username, String groupName) {
 * groupMembers.computeIfAbsent(groupName, k -> new
 * ArrayList<>()).add(username); }
 * 
 * public static void removeUserFromGroup(String username, String groupName) {
 * List<String> members = groupMembers.get(groupName); if (members != null) {
 * members.remove(username); if (members.isEmpty()) {
 * groupMembers.remove(groupName); } } } }
 */