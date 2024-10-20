import React, { useState, useEffect } from 'react';
import { Box, Heading, SimpleGrid, Stat, StatLabel, StatNumber, StatHelpText, VStack, Text, Button } from '@chakra-ui/react';
import axios from 'axios';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalAgreements: 0,
    pendingAgreements: 0,
    acceptedAgreements: 0,
    rejectedAgreements: 0,
  });
  const [attentionNeeded, setAttentionNeeded] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const statsResponse = await axios.get('/api/admin/dashboard/stats');
      setStats(statsResponse.data);

      const attentionResponse = await axios.get('/api/admin/dashboard/attention-needed');
      setAttentionNeeded(attentionResponse.data);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Admin Dashboard</Heading>
      <SimpleGrid columns={{ base: 1, md: 2, lg: 4 }} spacing={10}>
        <Stat>
          <StatLabel>Total Agreements</StatLabel>
          <StatNumber>{stats.totalAgreements}</StatNumber>
        </Stat>
        <Stat>
          <StatLabel>Pending Agreements</StatLabel>
          <StatNumber>{stats.pendingAgreements}</StatNumber>
          <StatHelpText>Awaiting response</StatHelpText>
        </Stat>
        <Stat>
          <StatLabel>Accepted Agreements</StatLabel>
          <StatNumber>{stats.acceptedAgreements}</StatNumber>
        </Stat>
        <Stat>
          <StatLabel>Rejected Agreements</StatLabel>
          <StatNumber>{stats.rejectedAgreements}</StatNumber>
        </Stat>
      </SimpleGrid>

      <VStack align="stretch" mt={10} spacing={4}>
        <Heading size="md">Agreements Needing Attention</Heading>
        {attentionNeeded.map((agreement) => (
          <Box key={agreement.id} p={4} borderWidth={1} borderRadius="md">
            <Text fontWeight="bold">{agreement.name}</Text>
            <Text>Status: {agreement.status}</Text>
            <Text>Reason: {agreement.attentionReason}</Text>
            <Button size="sm" colorScheme="blue" mt={2}>
              View Details
            </Button>
          </Box>
        ))}
      </VStack>
    </Box>
  );
};

export default Dashboard;
